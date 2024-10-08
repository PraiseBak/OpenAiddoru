package com.aiddoru.dev.Service.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.aiddoru.dev.Domain.Entity.Recommend.TagStatistic;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Persistence.Rank.TagRepository;
import com.aiddoru.dev.Persistence.TagStatisticRepository;
import com.aiddoru.dev.Persistence.User.UserRepository;
import com.aiddoru.dev.Utility.Page.PageUtility;
import com.aiddoru.dev.Utility.RedisHelper;
import com.aiddoru.dev.Utility.Tag.TagHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TagService {
    private final TagRepository tagRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final TagStatisticRepository tagStatisticRepository;
    private final IdolService idolService;
    public static final int MIN_TAG_SIZE = 100;



    /**
     * 유저가 관심있어하는 태그를 기반으로
     *      * idol을 추천해주는 기능입니다
     *      * 유저가 관심있어한다는 것은 해당 idol을 클릭하였을때 해당 idol의 태그를 가지고와
     *      * 다른 idol들과 비교해서 태그가 비슷한 idol을 추천해줍니다
     *      *
     *      * 한번 추천된 idol은 10번동안은 추천하지 않을껄
     *
     * within redis *
     * history를 살펴보고
     * redis에서 zSet을 이용해 빠르게 10개만 가지고 와서
     * 태그가 일치하는 idol중 order by subscriber count limit 10 적용하여 추천함
     *
     *
     * 해당 유저가 관심있는 태그에 대한 유저 추천
     * @param userId
     * @param tagSet
     * @return
     */
    @Transactional
    public List<Idol> recommendIdolByTag(String userId, Set<ZSetOperations.TypedTuple<Object>> tagSet) {
        String redisUserId = userId + RedisHelper.idolUserSetIdPostfix;
        List<Idol> resultArrayList = new ArrayList<>();
        String[] countryCodeList = {"KR","US","JP"};

        //태그에 대해서 가중치로 정렬된 10개의 tuple
        for (ZSetOperations.TypedTuple<Object> tuple : tagSet) {
            // 태그의 이름 가져오기
            String tagName = (String) tuple.getValue();
            // 태그의 점수 (가중치) 가져오기
            // Redis에서 태그에 매핑된 아이돌 리스트 조회
            List<Idol> idolList = getIdolsByTag(tagName, countryCodeList[0]);

            boolean recommended = tagExtinct(idolList, redisUserId, resultArrayList);
            //한개씩만 추천하도록 수정
            if(!resultArrayList.isEmpty()) break;
        }


        //추천안된 경우
        //구독자순 idol 중 하나 추천
        if(resultArrayList.isEmpty()){
            List<Idol> recommendIdolList = idolService.getVTuberListOrderByRanking(0,"");
            tagExtinct(recommendIdolList, redisUserId,resultArrayList);
        }

        return resultArrayList;
    }


    private boolean tagExtinct(List<Idol> idolList, String redisUserId, List<Idol> resultArrayList) {
        for(Idol idol : idolList){
            //userId로 참조해서 idolId 이미 있는지 확인
            if(Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(redisUserId, idol.getId() + ""))) {
                continue;
            }
            resultArrayList.add(idol);
            redisTemplate.opsForSet().add(redisUserId, idol.getId() + "");
            redisTemplate.expire(redisUserId, 1, TimeUnit.HOURS);
            return true;
        }
        return false;
    }

    private List<Idol> getIdolsByTag(String idolTag, String country) {
        if(country == null){
            return tagRepository.findTop100ByIdolIsNotNullAndTagAndCreatedDateAfterOrderByWeightDesc(idolTag, TagHelper.getTagStandardDate()).stream()
                    .map(Tag::getIdol)
                    .collect(Collectors.toList());
        }


        //idol tag가 일치하면서 idolId가 null이 아닌애들 가져오기
        return tagRepository.findTop100ByIdolIsNotNullAndTagAndCreatedDateAfterAndIdolCountryOrderByWeightDesc(idolTag, TagHelper.getTagStandardDate() ,country).stream()
                .map(Tag::getIdol)
                .collect(Collectors.toList());
    }


    /**
     * recommendIdolByTag와 유사하게
     * idol을 추천하는것이 아닌 video를 추천하며 태그 및 시청자가 많은순으로 추천합니다
     *
     * @return
     */
    public List<IdolVideoInfo> recommendVideoByTag() {
        return new ArrayList<>();
    }


    public Set<ZSetOperations.TypedTuple<Object>> getTagListByUserHistory(String userId) {
        String redisUserId = userId + RedisHelper.tagWeightSetUserIdPostfix;
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = redisTemplate.opsForZSet().rangeWithScores(redisUserId, 0, MIN_TAG_SIZE);
        if(typedTuples == null || typedTuples.size() < MIN_TAG_SIZE){
            setInitTagList(userId);
        }
        return redisTemplate.opsForZSet().rangeWithScores(redisUserId, 0, MIN_TAG_SIZE);
    }


    /**
     * @param userId
     */
    public void setInitTagList(String userId) {
        String redisUserId = userId + RedisHelper.tagWeightSetUserIdPostfix;
        List<Tag> initTagList = tagRepository.findTop100ByIdolIsNotNullAndCreatedDateAfterOrderByWeightDesc(TagHelper.getTagStandardDate());
        for(Tag tag : initTagList){
            redisTemplate.opsForZSet().add(redisUserId, tag.getTag(), tag.getWeight());
            redisTemplate.expire(redisUserId, 1, TimeUnit.HOURS);
        }
    }


    @Scheduled(cron = "30 46 18 * * *")
    @Transactional
    public void updateTagStatisticSchedule(){
        List<Tag> tagList = tagRepository.findAll();
        for(Tag tag : tagList ){
            String tagStr = tag.getTag();
            int weight = tag.getWeight();
            updateTagStatistic(tagStr,weight);
        }
    }


    @Transactional
    public void updateTagStatistic(String tagStr, int weight) {
        TagStatistic tagStatistic = getCreatedOrExistsTagStatistic(tagStr);
        tagStatistic.updateWeight(tagStatistic.getWeight() + weight);

    }

    /**
     * TagStatistic을 가져와 업데이트하는 메서드입니다
     * 초기값을 0으로하고 updateWeight에서 업데이트합니다
     * @param tagStr
     * @return
     */

    private TagStatistic getCreatedOrExistsTagStatistic(String tagStr) {
        return tagStatisticRepository.getTagStatisticByTag(tagStr.toLowerCase())
                .orElse(TagStatistic
                        .builder()
                        .weight(0L)
                        .tag(tagStr)
                        .build());
    }

}

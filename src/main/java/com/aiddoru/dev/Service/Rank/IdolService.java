package com.aiddoru.dev.Service.Rank;

import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

import com.aiddoru.dev.Domain.Entity.Rank.BlockedIdol;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeChannelAPI;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeSearchAPI;
import com.aiddoru.dev.Persistence.BlockedIdolRepository;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Utility.Page.PageUtility;
import com.google.api.services.youtube.model.Channel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdolService{
    private final IdolRepository idolRepository;
    private final YoutubeSearchAPI youtubeSearchAPI;
    private final YoutubeChannelAPI youtubeChannelAPI;
    private final IdolTodayService idolTodayService;
    private final BlockedIdolRepository blockedIdolRepository;
    private final IdolSubscriberTraceService idolSubscriberTraceService;
    private final TagService tagService;

    public List<Idol> getVTuberListOrderByRanking(long page, String country)
    {
        if(page > 10){
            page = 10;
        }
        Pageable pageable = PageUtility.getPageable(page);
        List<Idol> idolList = new ArrayList<>();
        if(country == null || country.isEmpty()){
            idolList = idolRepository.findAllByOrderBySubscriberCountDesc(pageable);
        }else{
            idolList = idolRepository.findAllByCountryOrderBySubscriberCountDesc(pageable,country);
        }


        //리스트를 제한된 개수만큼만 리턴하도록
        for(Idol idol : idolList){
            int idolTraceStatisticMaxLen =  Math.min(idol.getIdolSubscriberTraceList().size(), 30);
            List<IdolSubscriberTrace> idolSubscriberTraceList = idol.getIdolSubscriberTraceList().subList(0,idolTraceStatisticMaxLen);
            Collections.reverse(idolSubscriberTraceList);
            idol.setIdolSubscriberTraceList(idolSubscriberTraceList);

            int idolVideoMaxNum = Math.min(idol.getIdolVideoInfoList().size(),10);
            List<IdolVideoInfo> idolVideoInfoList = idol.getIdolVideoInfoList().subList(0,idolVideoMaxNum);
            idol.setIdolVideoInfoList(idolVideoInfoList);
        }

        return idolList;
    }

    public List<Idol> getVTuberListOrderByRankingWithCustomPageable(Pageable pageable)
    {
        return idolRepository.findTop300ByOrderBySubscriberCountDesc(pageable);
    }

    public List<Idol> getVTuberListOrderByRanking()
    {
        return idolRepository.findTop300ByOrderBySubscriberCountDesc();
    }


    //idol을 매일매일 갱신함
    //전날대비 갱신도 이때 해준다
    @Transactional
    public void saveIdol(Channel channel)
    {
        Idol idol = Idol.channelToIdol(channel);

        //5000미만 안받게 수정
        if(idol.getSubscriberCount().compareTo(BigInteger.valueOf(5000L)) < 0){
            log.info("5000미만이어 안받음");
            return;
        }

        Optional<Idol> idolOptional = idolRepository.findByChannelId(idol.getChannelId());

        if(blockedIdolRepository.findByBlockedIdolName(idol.getChannelName()).isPresent()) return;

        //영속성 유지된 아이돌
        Idol existsIdol;

        if (idolOptional.isEmpty())
        {
            idol.addIdolToday();
            existsIdol = idolRepository.save(idol);
            idolRepository.flush();
        }else{
            existsIdol = idolOptional.get();
            if(existsIdol.getIdolToday() == null){
                existsIdol.addIdolToday();
                idolRepository.flush();
            }

            //상승세는 일정구독자이상일때만 관심있음
            if(existsIdol.getSubscriberCount().compareTo(BigInteger.valueOf(5000L)) >= 0){
                idolTodayService.calSubscriberCountDiffPercent(existsIdol,idol.getSubscriberCount(),existsIdol.getSubscriberCount());
            }
            existsIdol.updateExistIdol(idol.getSubscriberCount(),idol.getViewCount(),idol.getVideoCount(),idol.getChannelThumbnailURL());
        }

        for(Tag tag : idol.getTagList()){
            tag.setIdol(existsIdol);
            tagService.saveTag(tag);
        }

        idolSubscriberTraceService.saveSubscriberTrace(existsIdol);
    }


    @Transactional
    public List<String> getSearchChannelIdList(){
        try {
            return youtubeSearchAPI.getSearchChannelIdList();
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public Idol findById(Long idolId){
        return idolRepository.findById(idolId).get();
    }

    public List<Idol> getVTuberListOrderByRankingAndNoCountry(long page) {
        Pageable pageable = PageUtility.getPageable(page);
        return idolRepository.findTop300ByCountryOrderBySubscriberCountDesc(pageable,null);
    }


    //Todo 이름을 수정하면 무슨일이 생기는지
    @Transactional
    public void modifyIdol(Idol idol) {
        Idol originIdol = idolRepository.findById(idol.getId()).orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));

        if(idol.getChannelName() != null) {
            originIdol.setChannelName(idol.getChannelName());
        }
        if(idol.getCountry() != null) {
            originIdol.setCountry(idol.getCountry());
        }
    }

    @Transactional
    public void deleteIdol(Long id) {
        Idol deleteIdol = idolRepository.findById(id).orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
        idolRepository.deleteById(deleteIdol.getId());

        String deleteIdolName = deleteIdol.getChannelName();
        BlockedIdol blockedIdol = new BlockedIdol();
        blockedIdol.setBlockedIdolName(deleteIdolName);
        blockedIdolRepository.save(blockedIdol);
    }

    /*
    starting with으로 안나오면 findByNameContaining으로 뚜따
     */
    public Idol findByIdolName(String recommendedAiddoruName) {
        Optional<Idol> idolOptional = idolRepository.findByChannelNameStartingWith(recommendedAiddoruName);
        return idolOptional.orElseGet(() -> idolRepository.findDistinctByChannelNameContaining(recommendedAiddoruName)
                .orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_RESOURCE.modifyStr("아이돌이 존재하지 않습니다"))));
    }

}

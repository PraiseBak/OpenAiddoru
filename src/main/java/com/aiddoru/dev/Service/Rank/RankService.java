package com.aiddoru.dev.Service.Rank;


import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeChannelAPI;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeSearchAPI;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Utility.Rank.RankUtility;
import com.aiddoru.dev.Utility.YoutubeAPI.ScheduleUtil;
import com.aiddoru.dev.Utility.YoutubeAPI.YoutubeAPIUtility;
import com.google.api.services.youtube.model.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RankService {
    
    private final YoutubeChannelAPI youtubeChannelAPI;
    private final IdolService idolService;
    private final IdolRepository idolRepository;
    private final IdolTodayService idolTodayService;
    private final IdolVideoInfoService idolVideoInfoService;
    private final InitRankingService initRankingService;
    private final YoutubeAPIUtility youtubeAPIUtility;

    //channelId를 통해 channel을 가져와 idol로 변환하여 저장합니다
    public void updateIdolFromChannelIdsQuery(List<String> channelIdsQueryList){
        for (String channelIdsQuery : channelIdsQueryList){
            List<Channel> channelResultList = youtubeChannelAPI.getChannelListFromChannelIdsQuery(channelIdsQuery);
            for(Channel channel : channelResultList){

                try{
                    idolService.saveIdol(channel);
                }catch (RuntimeException runtimeException){
                    log.error(String.format("updateIdolFromChannelIdsQuery => save idol중 에러 발생 Chnanel 정보 = %s \n 에러 = %s" ,channel.toString(),runtimeException.getMessage()));
                }
            }
        }
    }

    @Scheduled(cron = "30 46 15 * * *")
    public void updateIdolCountProperties()
    {
//        if(!ScheduleUtil.isIdolScheduleRunnable()) return;
        if(initRankingService.setInitRanking()) return;

        List<Idol> updateIdolList;
        //300명 가져옴
        updateIdolList = idolRepository.findTop300ByOrderBySubscriberCountDesc();
        //300명 가져와서 중복 제거하기
        List<Idol> koreanIdolList = idolRepository.findTop300ByCountryOrderBySubscriberCountDesc("KR");
        updateIdolList = RankUtility.getDuplicateRemoveIdolList(updateIdolList,koreanIdolList);

        idolTodayService.initIdolToday();
        List<String> channelIdList = new ArrayList<>();
        updateIdolList.forEach((idol) -> channelIdList.add(idol.getChannelId()));

        //최대 300명의 리스트 가져온다
        List<String> channelIdsQueryList = youtubeAPIUtility.listSplitByUnitNum(channelIdList,50);


        updateIdolFromChannelIdsQuery(channelIdsQueryList);

        //최근 영상 출력
        int recentVideoKoreanSearchStartIdx = ScheduleUtil.getVideoScheduleStartUnitNumForKorean();
        int recentVideoSearchStartIdx = ScheduleUtil.getVideoScheduleStartUnitNum();

        List<Idol> koreanYoutuberList = idolRepository.findTop300ByCountryOrderBySubscriberCountDesc("KR");

        //한국 유튜버 30명 영상 갱신하기
        List<String> koreanVideoSearchChannelIdList = koreanYoutuberList.subList(recentVideoKoreanSearchStartIdx,Math.min(recentVideoKoreanSearchStartIdx+30,koreanYoutuberList.size()))
                .stream()
                .map(Idol::getChannelId)
                .collect(Collectors.toList());
        idolVideoInfoService.updateRecentVideo(koreanVideoSearchChannelIdList);


        List<String> videoSearchChannelIdList = channelIdList.subList(recentVideoSearchStartIdx,recentVideoSearchStartIdx+50);
        idolVideoInfoService.updateRecentVideo(videoSearchChannelIdList);
    }


    public List<Idol> getRecentHotIdol(String country) {
        return idolTodayService.getRecentHotIdolList(country);
    }

    //search로 새 유저 찾아냄
    @Scheduled(cron = "30 46 16 * * *")
    public void updateRankBySearch() {
        List<String> channelIdList = idolService.getSearchChannelIdList();
        List<String> channelIdsQueryList = youtubeAPIUtility.listSplitByUnitNum(channelIdList,50);
        updateIdolFromChannelIdsQuery(channelIdsQueryList);
        log.info(String.format("update by rank search result list size = %d",channelIdsQueryList.size()));
    }

    public List<Idol> getMostClickedIdol(){
        List<Idol> idolList = new ArrayList<>();
        return idolTodayService.getMostClickedIdolList();
    }

}

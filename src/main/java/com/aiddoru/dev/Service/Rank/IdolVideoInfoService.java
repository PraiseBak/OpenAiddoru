package com.aiddoru.dev.Service.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeSearchAPI;
import com.aiddoru.dev.Persistence.Rank.IdolVideoInfoRepository;
import com.aiddoru.dev.Utility.Page.PageUtility;
import com.aiddoru.dev.Utility.YoutubeAPI.ScheduleUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class IdolVideoInfoService {
    private final IdolVideoInfoRepository idolVideoInfoRepository;
    private final YoutubeSearchAPI youtubeSearchAPI;
    private final IdolService idolService;

    public void save(IdolVideoInfo idolVideoInfo){
        idolVideoInfoRepository.save(idolVideoInfo);
    }


    /**
     * 300명을 대상으로 12일마다 로테이션으로 계산됩니다.
     * @param channelIdList
     */
    public void updateRecentVideo(List<String> channelIdList) {
        youtubeSearchAPI.updateVideoInfoByChannelIdList(channelIdList);
    }



    /**
     */
    public void updateRecentVideo(){
        try{
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            if(day % 4 != 0) return;

            int page = (day % 4) -1;

            log.info("오늘 일 = " + day);
            log.info("page = " + page);
            page = 0;

            //0에서 100
            Pageable pageable = PageUtility.getPageable(page, 100);
            List<Idol> idolList = idolService.getVTuberListOrderByRankingWithCustomPageable(pageable);
            List<String> channelIdList = new ArrayList<>();

            log.info("갱신 대상 aiddoru 개수 = " + idolList.size());

            int maxLen = idolList.size() >= 100 ? 100 : idolList.size();
            for(Idol idol : idolList.subList(0,maxLen)){
                channelIdList.add(idol.getChannelId());
            }

            log.info("비디오 업데이트되는 채널 개수 = " + channelIdList.size());
            youtubeSearchAPI.updateVideoInfoByChannelIdList(channelIdList);
        }catch (Exception e){
            log.error(e.getMessage());
            e.printStackTrace();
        }

    }

}

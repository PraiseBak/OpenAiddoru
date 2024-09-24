package com.aiddoru.dev.Service.Community;


import com.aiddoru.dev.DTO.Statistic.CommunityStatisticResponseDto;
import com.aiddoru.dev.DTO.Community.ThreadStatisticDto;
import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Domain.Entity.Community.CommunityStatistic;
import com.aiddoru.dev.Domain.Entity.Community.ThreadStatistic;
import com.aiddoru.dev.Persistence.Community.CommunityStatisticRepository;
import com.aiddoru.dev.Persistence.Community.ThreadStatisticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommunityStatisticService {

    private final CommunityStatisticRepository communityStatisticRepository;
    private final CommunityService communityService;
    private final ThreadStatisticRepository threadStatisticRepository;


    /*
    community에 오늘 작성된 threadCount
    3일마다 돌도록
     */
    @Scheduled(cron = "0 0 15 1/3 * ?")
    @Transactional
    public void updateCommunityStatistic(){
        for(Community community : communityService.getCommunityList("")){
            CommunityStatistic communityStatistic = community.getCommunityStatistic();
            communityStatistic.setPrevThreadCount(communityStatistic.getTodayThreadCount());
            communityStatistic.setTodayThreadCount(0L);
        }
    }

    /*
    커뮤니티 thread statistic 가져옴
     */
    public List<CommunityStatisticResponseDto> getCommunityStatisticList() {
        List<CommunityStatisticResponseDto> communityStatisticResponseDtoList = new ArrayList<>();
        for(CommunityStatistic communityStatistic : communityStatisticRepository.findTop10ByOrderByTodayThreadCountDesc()) {
            //0인건 출력안함
            if(communityStatistic.getTodayThreadCount() == 0) continue;
            CommunityStatisticResponseDto communityStatisticResponseDto = communityStatistic.toCommunityStatisticResponseDto();
            communityStatisticResponseDtoList.add(communityStatisticResponseDto);
        }
        return communityStatisticResponseDtoList;
    }

    /*
    조회수,좋아요많은 thread 조회
    */
    public List<ThreadStatisticDto> getPopularThreadList(boolean isVisitOrder) {
        List<ThreadStatistic> threadStatisticResponseList;
        List<ThreadStatisticDto> threadStatisticResponseDtoList = new ArrayList<>();

        if(isVisitOrder){
            //조회수순 10개
            threadStatisticResponseList = threadStatisticRepository.findTop10ByTodayVisitCountNotOrderByTodayVisitCount(0L);
        }else{
            //좋아요순 10개
            threadStatisticResponseList = threadStatisticRepository.findTop10ByTodayHeartCountNotOrderByTodayHeartCount(0L);
        }

        for (ThreadStatistic threadStatistic: threadStatisticResponseList) {
            threadStatisticResponseDtoList.add(ThreadStatisticDto.fromThreadStatistic(threadStatistic));
        }
        return threadStatisticResponseDtoList;
    }
}

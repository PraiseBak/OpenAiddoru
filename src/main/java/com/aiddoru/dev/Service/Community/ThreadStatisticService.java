package com.aiddoru.dev.Service.Community;

import com.aiddoru.dev.Domain.Entity.Community.ThreadStatistic;
import com.aiddoru.dev.Persistence.Community.ThreadStatisticRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThreadStatisticService {

    private final ThreadStatisticRepository threadStatisticRepository;

    @Transactional
    //3일마다 당일 조회수, 좋아요 초기화하기
    @Scheduled(cron = "0 0 15 1/3 * ?")
    public void initThreadStatistic(){
        List<ThreadStatistic> threadStatisticList = threadStatisticRepository.findAll();
        for(ThreadStatistic threadStatistic : threadStatisticList){
            threadStatistic.setTodayHeartCount(0L);
            threadStatistic.setTodayVisitCount(0L);
        }
    }

}

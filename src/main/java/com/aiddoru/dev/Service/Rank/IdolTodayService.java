package com.aiddoru.dev.Service.Rank;

import com.aiddoru.dev.DTO.Statistic.AiddoruStatisticResponseDto;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Rank.IdolToday;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Persistence.Rank.IdolTodayRepository;
import com.aiddoru.dev.Utility.YoutubeAPI.ScheduleUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IdolTodayService {

    private final IdolTodayRepository idolTodayRepository;
    private final IdolRepository idolRepository;


    /*
    전날대비 퍼센테이지 계산
     */
    @Transactional
    public void calSubscriberCountDiffPercent(Idol existsIdol, BigInteger newSubscriberCount, BigInteger prevSubscriberCount) {

        IdolToday idolToday = existsIdol.getIdolToday();
        BigInteger diff = newSubscriberCount.subtract(prevSubscriberCount);
        if(prevSubscriberCount == BigInteger.ZERO) prevSubscriberCount = BigInteger.valueOf(1);
        Double diffPercentage = (diff.doubleValue() / prevSubscriberCount.doubleValue()) * 100; // 구독자 수 변화 비율(%)
        diffPercentage = Math.floor(diffPercentage * 100) / 100;
        if(diffPercentage == 0) {
            diffPercentage = (double) 0;
        }

        idolToday.setSubscribeDiff(diff);
        idolToday.setSubscribeDiffPercentage(diffPercentage);
    }



    /*
    idol에 대한 click 이벤트
     */
    @Transactional
    public void clickTodayIdol(Idol idol){
        IdolToday idolToday = idol.getIdolToday();
        if(idolToday == null){
            idol.addIdolToday();
            idolToday = idol.getIdolToday();
        }
        idolToday.clickUpdate();
    }


    @Transactional
//    public void initIdolToday(List<Idol> updateIdolList) {
    public void initIdolToday() {
        for(IdolToday idolToday : idolTodayRepository.findAll()){
            if(ScheduleUtil.initIdolTodayClicked()){
                idolToday.setTodayClicked(0L);
            }
            idolToday.setSubscribeDiffPercentage((double) 0);
            idolToday.setSubscribeDiff(BigInteger.valueOf(0));
        }
    }

    /*
    국가 및 상승세(구독자 퍼센트) 기준으로 출력
     */
    public List<Idol> getRecentHotIdolList(String country) {
        if(country.length() == 0 || country == null){
            return idolTodayRepository.findTop10BySubscribeDiffPercentageNotOrderBySubscribeDiffPercentageDesc(0).stream()
                    .map(IdolToday::getIdol)
                    .collect(Collectors.toList());
        }

        return idolTodayRepository.findTop10BySubscribeDiffPercentageNotAndIdolCountryOrderBySubscribeDiffPercentageDesc(0,country).stream()
                    .map(IdolToday::getIdol)
                    .collect(Collectors.toList());
    }

    /*
    idol 저장
     */
    public IdolToday saveIdolToday(IdolToday idolToday) {
        return idolTodayRepository.save(idolToday);
    }

    public List<Idol> getMostClickedIdolList() {
        List<IdolToday> idolTodayList = idolTodayRepository.findTop10ByTodayClickedNotOrderByTodayClicked(0L);
        List<Idol> idolList = idolTodayList.stream()
                .map((idolToday) -> idolToday.getIdol())
                .collect(Collectors.toList());
        return idolList;
    }
}

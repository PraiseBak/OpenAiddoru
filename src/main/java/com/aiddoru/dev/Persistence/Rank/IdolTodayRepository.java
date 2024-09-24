package com.aiddoru.dev.Persistence.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.IdolToday;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;

public interface IdolTodayRepository extends JpaRepository<IdolToday,Long> {

    //idol update용
    List<IdolToday> findTop10ByOrderBySubscribeDiffPercentageDesc();
    //구독자 퍼센트변화 (0아닌값들 중 )정렬 및 국가별로 가져옴
    List<IdolToday> findTop10BySubscribeDiffPercentageNotAndIdolCountryOrderBySubscribeDiffPercentageDesc(double value,String country);

    ArrayList<IdolToday> findTop10BySubscribeDiffPercentageNotOrderBySubscribeDiffPercentageDesc(double value);

    List<IdolToday> findTop10ByTodayClickedNotOrderByTodayClicked(Long todayClicked);


}

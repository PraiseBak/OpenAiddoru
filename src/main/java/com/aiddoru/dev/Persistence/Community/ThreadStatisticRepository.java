package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.ThreadStatistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThreadStatisticRepository extends JpaRepository<ThreadStatistic,Long>
{
    List<ThreadStatistic> findTop10ByOrderByTodayVisitCount();

    List<ThreadStatistic> findTop10ByTodayHeartCountNotOrderByTodayHeartCount(Long val);

    List<ThreadStatistic> findTop10ByTodayVisitCountNotOrderByTodayVisitCount(Long val);
}

package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.CommunityStatistic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommunityStatisticRepository extends JpaRepository<CommunityStatistic,Long> {
    public List<CommunityStatistic> findTop10ByOrderByTodayThreadCountDesc();
}



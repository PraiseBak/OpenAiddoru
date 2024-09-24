package com.aiddoru.dev.Persistence;

import com.aiddoru.dev.Domain.Entity.Recommend.TagStatistic;
import org.apache.catalina.LifecycleState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagStatisticRepository extends JpaRepository<TagStatistic,Long> {
    public Optional<TagStatistic> getTagStatisticByTag(String tag);


}

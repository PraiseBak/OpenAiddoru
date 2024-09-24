package com.aiddoru.dev.Persistence.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdolSubscriberTraceRepository extends JpaRepository<IdolSubscriberTrace,Long> {
}

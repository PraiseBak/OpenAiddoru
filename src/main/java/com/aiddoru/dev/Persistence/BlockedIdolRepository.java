package com.aiddoru.dev.Persistence;

import com.aiddoru.dev.Domain.Entity.Rank.BlockedIdol;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedIdolRepository extends JpaRepository<BlockedIdol,Long> {

    Optional<BlockedIdol> findByBlockedIdolName(String channelName);

}

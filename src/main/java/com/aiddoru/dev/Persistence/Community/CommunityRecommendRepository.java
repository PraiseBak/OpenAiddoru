package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.CommunityRecommend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRecommendRepository extends JpaRepository<CommunityRecommend,Long> {
    CommunityRecommend findByCommunityName(String communityName);


}

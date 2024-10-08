package com.aiddoru.dev.Persistence.Community;

import com.aiddoru.dev.Domain.Entity.Community.Thread;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ThreadRepository extends JpaRepository<Thread,Long>
{
    Optional<Thread> findByIdAndIsDeleted(Long id,boolean isDeleted);

    List<Thread> findAllByOrderByCreatedDateDesc(Pageable pageable);


    List<Thread> findByIsRecommendedOrderByCreatedDateDesc(Pageable pageable,boolean isRecommended);

    List<Thread> findByCommunity_IdAndIsDeletedFalseOrderByCreatedDateDesc(long communityId, Pageable pageable);

    List<Thread> findByCommunity_Id(Long communityId);

    List<Thread> findByCommunity_IdAndIsDeletedFalseAndIsRecommendedOrderByCreatedDateDesc(Long communityId,Pageable pageable,boolean isRecommended);


    @Query("SELECT COUNT(t) FROM Community c JOIN c.threadList t WHERE c.id = :communityId")
    long countThreadsByCommunityId(@Param("communityId") Long communityId);

    @Query("SELECT COUNT(t) FROM Community c JOIN c.threadList t WHERE c.id = :communityId AND t.isRecommended = :isRecommended")
    long countThreadsByCommunityIdAndIsRecommended(@Param("communityId") Long communityId, @Param("isRecommended") boolean isRecommended);

}

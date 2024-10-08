package com.aiddoru.dev.Persistence.Rank;

import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    List<Tag> findTop100ByIdolIsNotNullAndCreatedDateAfterOrderByWeightDesc(LocalDateTime createdDate);

    List<Tag> findTop100ByIdolIsNotNullAndTagAndCreatedDateAfterAndIdolCountryOrderByWeightDesc(String tag,LocalDateTime craetedDate,String country);
    List<Tag> findTop100ByIdolIsNotNullAndTagOrderByWeightDesc(String tag);
    List<Tag> findTop100ByIdolIsNotNullAndTagAndCreatedDateAfterOrderByWeightDesc(String tag, LocalDateTime createdDate);
}

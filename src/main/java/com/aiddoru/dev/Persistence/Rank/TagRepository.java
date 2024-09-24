package com.aiddoru.dev.Persistence.Rank;

import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {

    List<Tag> findTop100ByOrderByWeightDesc();

    List<Tag> findTop100ByIdolIsNotNullAndTagAndIdolCountryOrderByWeightDesc(String tag,String country);
    List<Tag> findTop100ByIdolIsNotNullAndTagOrderByWeightDesc(String tag);
}

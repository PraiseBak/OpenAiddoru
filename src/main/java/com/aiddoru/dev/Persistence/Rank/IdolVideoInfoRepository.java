package com.aiddoru.dev.Persistence.Rank;

import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdolVideoInfoRepository extends JpaRepository<IdolVideoInfo,Long> {
    Boolean existsByVideoThumbnailURL(String videoThumbnailURL);

    Boolean existsByVideoId(String videoId);

}

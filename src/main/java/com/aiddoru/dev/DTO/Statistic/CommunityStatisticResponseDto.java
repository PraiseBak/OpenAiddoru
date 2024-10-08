package com.aiddoru.dev.DTO.Statistic;


import com.aiddoru.dev.Domain.Entity.Community.Community;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommunityStatisticResponseDto {

    //오늘의 게시물수
    private Long todayThreadCount = 0L;

    //전날 게시물 수
    private Long prevThreadCount = 0L;

    private String communityName;

    private String communityURL;

    private Double diffPercentage;

    private String communityIdolThumbnail;


    /*
        Community에서 communityStatistic 가져오면 community의 나머지도 가져와서 별로임
        communityStatisticDto만들자그냥
    */

}

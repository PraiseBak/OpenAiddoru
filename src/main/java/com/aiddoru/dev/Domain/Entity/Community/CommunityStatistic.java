package com.aiddoru.dev.Domain.Entity.Community;


import com.aiddoru.dev.DTO.Statistic.CommunityStatisticResponseDto;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommunityStatistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //오늘의 게시물수
    private Long todayThreadCount = 0L;

    //전날 게시물 수
    private Long prevThreadCount = 0L;

    @OneToOne(fetch = FetchType.LAZY)
    private Community community;

    public CommunityStatisticResponseDto toCommunityStatisticResponseDto() {
        long diff = todayThreadCount - prevThreadCount;
        Double diffPercentage = null;

        if(diff == 0L){
            diffPercentage = (double) 0;
        }else{
            diffPercentage = ((double)diff / (double)(prevThreadCount == 0 ? 1 : prevThreadCount)) * 100;
            diffPercentage = Math.floor(diffPercentage * 100) / 100;
        }

        if(diffPercentage == 0L){
            diffPercentage = null;
        }

        return CommunityStatisticResponseDto.builder()
                .todayThreadCount(todayThreadCount)
                .prevThreadCount(prevThreadCount)
                .communityURL(community.getCommunityURL())
                .communityName(community.getCommunityName())
                .communityIdolThumbnail(community.getCommunityIdol() != null ? community.getCommunityIdol().getChannelThumbnailURL() : null)
                .diffPercentage(diffPercentage)
                .build();
    }

    public void addTodayThreadCount() {
        this.todayThreadCount++;
    }
}

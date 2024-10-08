package com.aiddoru.dev.DTO.Community;


import com.aiddoru.dev.Domain.Entity.Community.ThreadStatistic;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ThreadStatisticDto {

    private Long threadId;

    //오늘 조회수
    private Long todayVisitCount = 0L;

    //오늘 받은 좋아요 수
    private Long todayHeartCount = 0L;

    private String content;

    private String title;

    private String communityURL;

    private boolean isRecommend;

    private String username;

    private String communityIdolThumbnailURL;


    public static ThreadStatisticDto fromThreadStatistic(ThreadStatistic threadStatistic) {
        return ThreadStatisticDto.builder()
                .content(threadStatistic.getThread().getContent())
                .title(threadStatistic.getThread().getTitle())
                .isRecommend(threadStatistic.getThread().isRecommended())
                .todayHeartCount(threadStatistic.getTodayHeartCount())
                .todayVisitCount(threadStatistic.getTodayVisitCount())
                .communityURL(threadStatistic.getThread().getCommunity().getCommunityURL())
                .threadId(threadStatistic.getThread().getId())
                .username(threadStatistic.getThread().getUser().getUsername())
                .communityIdolThumbnailURL(threadStatistic.getThread().getCommunity().getCommunityIdol().getChannelThumbnailURL())
                .build();
    }

}

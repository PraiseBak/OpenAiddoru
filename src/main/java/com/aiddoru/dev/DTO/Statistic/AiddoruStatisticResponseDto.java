package com.aiddoru.dev.DTO.Statistic;

import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import com.aiddoru.dev.Domain.Entity.Rank.IdolToday;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class AiddoruStatisticResponseDto {
    private final IdolToday idolToday;
    private String channelName;
    private List<IdolVideoInfo> idolVideoInfoList;
    private String channelUrl;
    private String channelThumbnailURL;
    private IdolSubscriberTrace idolSubscriberTrace;
}

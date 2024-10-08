package com.aiddoru.dev.DTO;

import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import com.aiddoru.dev.Domain.Entity.Rank.IdolToday;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Utility.BigIntegerDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.micrometer.core.annotation.TimedSet;
import lombok.*;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CachedIdolDto {
    private Long id;
    private String channelName;
    private String channelId;

    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    private BigInteger subscriberCount;
//
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private BigInteger viewCount;
//
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    private BigInteger videoCount;

    private String channelUrl;
    private String channelThumbnailURL;
    private String country;
    private boolean isBlocked;
    private Date publishedAt;
    private String description;
    private IdolTodayDto idolToday;
    private List<IdolVideoInfoDto> idolVideoInfoList;
    private List<IdolSubscriberTraceDto> idolSubscriberTraceList;
}

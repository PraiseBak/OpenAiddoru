package com.aiddoru.dev.DTO;

import com.aiddoru.dev.Domain.Entity.BaseTimeEntity;
import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * IdolSubscriberTrace 엔티티를 위한 DTO입니다.
 * 이 DTO는 구독자 수와 조회 수를 전달하기 위해 사용됩니다.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdolSubscriberTraceDto extends BaseTimeEntity {

    @JsonProperty("id")  // JSON으로 직렬화/역직렬화 시 사용할 키
    private Long id;

    @JsonProperty("subscriberCount")  // JSON으로 직렬화/역직렬화 시 사용할 키
    private BigInteger subscriberCount;

    @JsonProperty("viewCount")  // JSON으로 직렬화/역직렬화 시 사용할 키
    private BigInteger viewCount;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;



    // 엔티티 리스트를 DTO 리스트로 변환하는 정적 메서드
    public static List<IdolSubscriberTraceDto> fromEntityList(List<IdolSubscriberTrace> entityList) {
        return entityList.stream()
                .map(trace -> IdolSubscriberTraceDto.builder()
                        .id(trace.getId())
                        .subscriberCount(trace.getSubscriberCount())
                        .createdDate(trace.getCreatedDate())
                        .modifiedDate(trace.getModifiedDate())
                        .viewCount(trace.getViewCount())
                        .build())
                .collect(Collectors.toList());
    }

}
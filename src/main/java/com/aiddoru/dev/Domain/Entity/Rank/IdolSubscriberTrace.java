package com.aiddoru.dev.Domain.Entity.Rank;

import com.aiddoru.dev.Domain.Entity.BaseTimeEntity;
import com.aiddoru.dev.Utility.BigIntegerDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDateTime;


/**
 * 최근 30일간 구독자 수를 추적하기 위한 엔티티입니다
 * idolToday를 계산할때 구독자수만 가져와 저장하도록 하였습니다
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdolSubscriberTrace extends BaseTimeEntity {
    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(0)
    @NotNull
    @Getter
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    private BigInteger subscriberCount;

    @Min(0)
    @NotNull
    @Getter
    @JsonSerialize(using = ToStringSerializer.class)
    @JsonDeserialize(using = BigIntegerDeserializer.class)
    private BigInteger viewCount;


}

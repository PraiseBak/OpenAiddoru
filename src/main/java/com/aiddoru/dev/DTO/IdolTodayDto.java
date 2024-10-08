package com.aiddoru.dev.DTO;

import com.aiddoru.dev.Domain.Entity.Rank.IdolToday;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdolTodayDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("subscribeDiff")
    private BigInteger subscribeDiff;

    @JsonProperty("subscribeDiffPercentage")
    private Double subscribeDiffPercentage;

    @JsonProperty("todayClicked")
    private Long todayClicked;

    @JsonProperty("idolId")  // Idol의 ID를 담기 위해 추가
    private Long idolId;

    // 엔티티를 DTO로 변환하는 메서드
    public static IdolTodayDto fromEntity(IdolToday idolToday) {
        return IdolTodayDto.builder()
                .id(idolToday.getId())
                .subscribeDiff(idolToday.getSubscribeDiff())
                .subscribeDiffPercentage(idolToday.getSubscribeDiffPercentage())
                .todayClicked(idolToday.getTodayClicked())
                .idolId(idolToday.getIdol() != null ? idolToday.getIdol().getId() : null) // Idol이 null일 경우를 고려
                .build();
    }
}
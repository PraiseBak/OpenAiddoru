package com.aiddoru.dev.DTO;

import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("tag")
    private String tag;

    @JsonProperty("weight")
    private Integer weight;

    // 엔티티를 DTO로 변환하는 메서드
    public static TagDto fromEntity(Tag tag) {
        return TagDto.builder()
                .id(tag.getId())
                .tag(tag.getTag())
                .weight(tag.getWeight())
                .build();
    }
}

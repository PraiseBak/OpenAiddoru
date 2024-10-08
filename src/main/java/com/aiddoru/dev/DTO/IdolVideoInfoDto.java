package com.aiddoru.dev.DTO;

import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdolVideoInfoDto {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("videoId")
    private String videoId;

    @JsonProperty("videoThumbnailURL")
    private String videoThumbnailURL;

    @JsonProperty("publishedAt")
    private LocalDateTime publishedAt;

    @JsonProperty("tags")  // Tag의 리스트를 담기 위해 추가
    private List<TagDto> tags;

    // 엔티티를 DTO로 변환하는 메서드
    public static IdolVideoInfoDto fromEntity(IdolVideoInfo idolVideoInfo) {
        List<TagDto> tagDtos = idolVideoInfo.getTagList().stream()
                .map(TagDto::fromEntity)  // Tag DTO로 변환
                .collect(Collectors.toList());

        return IdolVideoInfoDto.builder()
                .id(idolVideoInfo.getId())
                .videoId(idolVideoInfo.getVideoId())
                .videoThumbnailURL(idolVideoInfo.getVideoThumbnailURL())
                .publishedAt(idolVideoInfo.getPublishedAt())
                .tags(tagDtos)
                .build();
    }
}

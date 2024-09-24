package com.aiddoru.dev.DTO.Community;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommunityResponseDto {
    private Long id;
    private String communityName;
    private String communityURL;
    private boolean isBlocked;
    private List<ThreadResponseDto> threadList;
    private long totalPage;

    public void setTotalPage(long totalPage) {
        this.totalPage = totalPage;
    }
}

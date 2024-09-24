package com.aiddoru.dev.DTO.Community;


import com.aiddoru.dev.Domain.Entity.Community.Comment;
import com.aiddoru.dev.Domain.Entity.Community.Heart;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ThreadResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    //    private List<Comment> commentList;
    private List<CommentDto> commentDtoList;
    private List<Heart> heartList = new ArrayList<>();
    boolean isDeleted = false;
    boolean isRecommended = false;
    int visitCount = 0;
    int likeCount = 0;
    private String communityName;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public static ThreadResponseDto fromThread(Thread thread) {
        setIPInvisible(thread.getHeartList());

        List<CommentDto> convertedCommentList = new ArrayList<>();
        for(Comment comment : thread.getCommentList()){
            CommentDto commentDto = comment.commentToDto();
            convertedCommentList.add(commentDto);
            setIPInvisible(comment.getHeartList());
        }

        int likeCount = 0;
        for(Heart heart : thread.getHeartList()){
            if(heart.isPositive()){
                likeCount++;
            }
        }

        return ThreadResponseDto.builder()
                .id(thread.getId())
                .title(thread.getTitle())
                .content(thread.getContent())
                .username(thread.getUser().getUsername()) // 예시로 작성
                .commentDtoList(convertedCommentList)
                .heartList(thread.getHeartList())
                .isDeleted(thread.isDeleted())
                .visitCount(thread.getVisitCount())
                .likeCount(likeCount)
                .communityName(thread.getCommunity().getCommunityName())
                .createdDate(thread.getCreatedDate())
                .modifiedDate(thread.getModifiedDate())
                .isRecommended(thread.isRecommended())
                .build();
    }

    private static void setIPInvisible(List<Heart> heartList){
        for (Heart h:heartList)
        {
            h.setIp(extractSecondSegment(h.getIp()));
        }
    }

    public static String extractSecondSegment(String ipAddress) {
        int firstDotIndex = ipAddress.indexOf(".");
        if (firstDotIndex != -1) {
            int secondDotIndex = ipAddress.indexOf(".", firstDotIndex + 1);
            if (secondDotIndex != -1) {
                return ipAddress.substring(0, secondDotIndex) + ".***.***"; // ;
            }
        }
        return ipAddress + ".***.***"; // 두 번째 점(.) 이전의 문자열이 없을 경우 원래 문자열을 반환
    }

}

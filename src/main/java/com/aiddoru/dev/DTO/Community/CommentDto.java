package com.aiddoru.dev.DTO.Community;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class CommentDto {
    private final Long id;

    private final String content;

    // 필요한 경우 사용자(User) 정보를 가진 필드도 추가할 수 있습니다.
    private final Long userId;

    private Long positiveHeartCount;

    private Long negativeHeartCount;


    private final String username;

    private List<CommentDto> subComment;

    //is는 프론트에서 안받아주는듯
    @JsonProperty("isSubComment")
    private final boolean isSubComment;

    public void setPositiveHeartCount(long positiveHeartCount){
        this.positiveHeartCount = positiveHeartCount;
    }

    public void setNegativeHeartCount(long negativeHeartCount){
        this.negativeHeartCount = negativeHeartCount;
    }

    public void setSubCommentList(List<CommentDto> subCommentDtoList) {
        this.subComment = subCommentDtoList;
    }
}


/*


기초생명공학
세포 조직 기관 시상하부
호르몬 심폐계

이미지,기억,공책

항상성

식욕촉진 억제 누가하는지`
호르몬 누가보내고 누가 받고
랩틴 식욕감퇴
그램린 식욕촉진

항상성

negative feedback


뼈 근육 연골 인대

뇌에 호르몬이제일많이나간다

그렐린 위에서나와서 ~~~
12시에서 2시피크니까 잠좀일찍

암

심장 = 근육 기능떨어지는이유
선천적,많이먹기로

당뇨어쩌구저쩌구

인슐린 스파이크 -> 저항성생김 -> 당뇨


인슐린 -> 저장에가깝다 = 그랠린

캡톤시스를 만들면 살이잘빠

 */

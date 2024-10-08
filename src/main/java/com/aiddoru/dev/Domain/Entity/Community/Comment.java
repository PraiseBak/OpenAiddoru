package com.aiddoru.dev.Domain.Entity.Community;

import com.aiddoru.dev.DTO.Community.CommentDto;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Comment
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 2)
    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
//    @JsonManagedReference(value = "comment-user")
//    @JsonBackReference(value = "user-comment")
    private User user;

    @JoinColumn(name = "heart_id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    @JoinColumn(name = "parent_comment_id")
    @OneToMany(cascade = CascadeType.ALL)
    private List<Comment> subCommentList = new ArrayList<>();

    private boolean isParent;

    private boolean isSubComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id")
    @JsonBackReference(value = "thread-comment")
    private Thread thread;

    public void setUser(User user) {
        this.user = user;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public void addSubComment(Comment subComment) {
        this.getSubCommentList().add(subComment);
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setIsSubComment(boolean isChild) {
        this.isSubComment = isChild;
    }


    public CommentDto commentToDto() {
        long positiveHeartCount = getPositiveHeartCount();
        long negativeHeartCount = heartList.size() - positiveHeartCount;
        List<CommentDto> subCommentDtoList = getCommentListToDto(subCommentList);

        CommentDto curCommentDto = getCommentDtoFromComment(this);
        curCommentDto.setPositiveHeartCount(positiveHeartCount);
        curCommentDto.setNegativeHeartCount(negativeHeartCount);
        curCommentDto.setSubCommentList(subCommentDtoList);


        return curCommentDto;
    }
    private CommentDto getCommentDtoFromComment(Comment comment){
        CommentDto commentDto = CommentDto.builder()
                .id(comment.id)
                .content(comment.content)
                .userId(comment.user != null ? comment.user.getId() : null) // Assuming User has an id field
                .username(comment.user != null ? comment.user.getUsername() : null) // Assuming User has a username field
                .isSubComment(comment.isSubComment)
                .build();
        return commentDto;
    }

    private List<CommentDto> getCommentListToDto(List<Comment> commentList){
        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment comment : commentList){
            long positiveHeartCount = comment.getPositiveHeartCount();
            long negativeHeartCount = comment.heartList.size() - positiveHeartCount;

            CommentDto curCommentDto = getCommentDtoFromComment(comment);
            curCommentDto.setPositiveHeartCount(positiveHeartCount);
            curCommentDto.setNegativeHeartCount(negativeHeartCount);
            //subComment의 subComment는 없음!!!
            curCommentDto.setSubCommentList(null);
            commentDtoList.add(curCommentDto);
        }
        return commentDtoList;

    }

    // You can implement methods to calculate positive and negative heart countsp here
    private Long getPositiveHeartCount() {
        long count = 0;
        for(Heart heart : heartList){
            if(heart.isPositive()){
                count++;
            }
        }
        return count;
    }

}

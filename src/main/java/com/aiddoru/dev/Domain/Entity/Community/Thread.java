package com.aiddoru.dev.Domain.Entity.Community;

import com.aiddoru.dev.DTO.Community.ThreadResponseDto;
import com.aiddoru.dev.Domain.Entity.BaseTimeEntity;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Thread extends BaseTimeEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2,max = 40)
    private String title = "";

    @Size(min = 2)
    @Column(columnDefinition = "TEXT")
    private String content = "";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @JsonManagedReference(value = "thread-comment")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "thread")
    private List<Comment> commentList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "hearts_id")
    private List<Heart> heartList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "community_id")
    @JsonIgnore
    private Community community;

    boolean isDeleted = false;

    private boolean isRecommended;

    private int visitCount = 0;

    @OneToOne(fetch = FetchType.LAZY,optional = false,cascade = CascadeType.ALL)
    @JsonIgnore
    private com.aiddoru.dev.Domain.Entity.Community.ThreadStatistic threadStatistic;

    public void setUser(User user)
    {
        this.user = user;
    }

    public void addComment(Comment comment)
    {
        this.commentList.add(comment);
        comment.setThread(this);
    }

    public void setDelete() {
        this.isDeleted = true;

    }

    public void setTitle(String title) {
        setModifiedDate(LocalDateTime.now());
        this.title = title;
    }


    public void setContent(String content)
    {
        setModifiedDate(LocalDateTime.now());
        this.content = content;
    }

    public ThreadResponseDto toThreadResponseDto(){
        return ThreadResponseDto.fromThread(this);
    }

    @Transactional
    public void setRecommend() {
        this.isRecommended = true;
    }

    public void setCommunity(Community community) {
        this.community = community;
    }

    public void addVisitCount() {
        this.community.addThreadReadCount();
        this.visitCount++;
        this.threadStatistic.addTodayVisitCount();
    }

    public void setThreadStatistic(ThreadStatistic threadStatistic) {
        this.threadStatistic = threadStatistic;
        threadStatistic.setThread(this);
    }
}

package com.aiddoru.dev.Domain.Entity.Community;

import com.aiddoru.dev.DTO.Community.CommunityResponseDto;
import com.aiddoru.dev.DTO.Community.ThreadResponseDto;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Community
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2,max = 40)
    private String communityName = "";

    @Size(min = 2,max = 40)
    private String communityURL = "";

    //커뮤니티의 차단여부
    boolean isBlocked;

    //연관 엔티티가 수정될 경우 같이 수정되게 하는 영속성 전이 설정
    @JoinColumn(name = "thread_id",nullable = false)
    @OneToMany(cascade=CascadeType.ALL,fetch = FetchType.LAZY)
    @Builder.Default
    private List<Thread> threadList = new ArrayList<>();

    //CommunityStatistic -> Community로만 참조합니다
    //필수
    @OneToOne(fetch = FetchType.LAZY,optional = false,orphanRemoval = true)
    @JsonIgnore
    private CommunityStatistic communityStatistic;

    @OneToOne(orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Idol communityIdol;

    private String idolName = "";

    //총 조회수 count
    @Builder.Default
    private Long threadReadCount = 0L;

    public void addThreadReadCount(){
        this.threadReadCount++;
    }

    public void setThreadList(List<Thread> threadList){
        this.threadList = threadList;
    }

    public void addThread(Thread thread){
        this.getThreadList().add(thread);
    }

    public static CommunityResponseDto convertToCommunityResponseDto(Community community,List<Thread> pagedThreadList) {
        List<ThreadResponseDto> threadResponseDtoList = pagedThreadList
                .stream()
                .map(Thread::toThreadResponseDto)
                .collect(Collectors.toList());

//        List<ThreadResponseDto> threadResponseDtoList = community.getThreadList()
//                .stream()
//                .filter(thread -> thread.isDeleted == false)
//                .map(thread -> thread.toThreadResponseDto())
//                .collect(Collectors.toList());

        return CommunityResponseDto.builder()
                .id(community.getId())
                .communityName(community.getCommunityName())
                .communityURL(community.getCommunityURL())
                .isBlocked(community.isBlocked())
                .threadList(threadResponseDtoList)
                .build();
    }

    public void setCommunityStatistic(CommunityStatistic communityStatistic) {
        this.communityStatistic = communityStatistic;
        this.communityStatistic.setCommunity(this);
    }

    public void setIdol(Idol idol) {
        this.communityIdol = idol;
    }

}

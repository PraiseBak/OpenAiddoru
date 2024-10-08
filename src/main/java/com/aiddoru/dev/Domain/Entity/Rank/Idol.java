package com.aiddoru.dev.Domain.Entity.Rank;

import com.aiddoru.dev.DTO.CachedIdolDto;
import com.aiddoru.dev.DTO.IdolSubscriberTraceDto;
import com.aiddoru.dev.DTO.IdolTodayDto;
import com.aiddoru.dev.DTO.IdolVideoInfoDto;
import com.aiddoru.dev.Domain.Entity.BaseTimeEntity;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.aiddoru.dev.Utility.YoutubeAPI.YoutubeAPIUtility;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelSettings;
import com.google.api.services.youtube.model.ChannelSnippet;
import com.google.api.services.youtube.model.ChannelStatistics;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.logging.XMLFormatter;
import java.util.stream.Collectors;

/*
랭킹,추천에 사용되는 인플루언서 객체
구독자 수,조회수,좋아요,댓글 수, 생성 날짜
이름,태그,카테고리
기준별 순위는 미리 저장하지않고 계산해서 넣습니다
매일매일 추가된 데이터는 한달단위로 정리합니다(최대 한달까지만 select)
이미지 경로

또한 해당 엔티티는 엔티티지만 모든 정보를 오픈해도 되는 프로퍼티들만 있으므로 편의상 Data 어노테이션을 사용하겠습니다
 */
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(indexes = {
        @Index(name = "idx_subscribe_count", columnList = "subscriberCount"),
        @Index(name = "idx_country", columnList = "country")
})
public class Idol extends BaseTimeEntity{
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Idol idol = (Idol) o;
        return Objects.equals(id, idol.id) && Objects.equals(channelId, idol.channelId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, channelId);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String channelName;

    @NotNull
    @NotBlank
    private String channelId;

    @NotNull
    @Min(0)
    private BigInteger subscriberCount;

    @Min(0)
    @NotNull
    private BigInteger viewCount;

    @Min(0)
    @NotNull
    private BigInteger videoCount;

    @NotNull
    @NotBlank
    private String channelUrl;

    private String channelThumbnailURL = "";

    private String country;
    private boolean isBlocked;

    @NotNull
    private Date publishedAt;

    @Column(length = 1000)
    private String description = "";

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_id")
    @JsonManagedReference
    private List<Tag> tagList = new ArrayList<>();

    @OneToOne(orphanRemoval = true,cascade = CascadeType.ALL)
    @JsonManagedReference
    private IdolToday idolToday;

    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_id")
    @OrderBy("createdDate DESC") // 날짜 기준으로 내림차순 정렬
    private List<IdolVideoInfo> idolVideoInfoList = new ArrayList<>();

    //날짜별로 구독자 순
    @OneToMany(orphanRemoval = true,cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "idol_id")
    @OrderBy("createdDate DESC") // 날짜 기준으로 내림차순 정렬
    private List<IdolSubscriberTrace> idolSubscriberTraceList = new ArrayList<>();

    private Date makeDateFromChannelSnippet(ChannelSnippet channelSnippet){
        DateTime publishedAt = channelSnippet.getPublishedAt();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try{
            date = dateFormat.parse(publishedAt.toString());
        }catch (ParseException parseException){
        }
        return date;
    }


    public void setIdolPropertiesFromChannelSnippet(ChannelSnippet channelSnippet){
        this.channelName = channelSnippet.getTitle();
        //수동으로 수정하는 경우가 있음
        if(this.country == null){
            this.country = channelSnippet.getCountry();
        }
        this.publishedAt = makeDateFromChannelSnippet(channelSnippet);
        this.channelName = channelSnippet.getTitle();
    }

    public void setIdolPropertiesFromStatistics(ChannelStatistics channelStatistics){
        this.videoCount = channelStatistics.getVideoCount();
        this.viewCount = channelStatistics.getViewCount();
        this.subscriberCount = channelStatistics.getSubscriberCount();

    }

    public static Idol channelToIdol(Channel channel){
        Idol idol = new Idol();
        idol.setIdolPropertiesFromChannelSnippet(channel.getSnippet());
        idol.setIdolPropertiesFromStatistics(channel.getStatistics());
        idol.setChannelId(channel.getId());
        idol.setChannelUrl("https://www.youtube.com/channel/" + channel.getId());
        idol.setChannelThumbnailURL(channel.getSnippet().getThumbnails().getDefault().getUrl());

        String description = channel.getSnippet().getDescription();
        idol.setDescription(description);
        ChannelSettings channelSettings = channel.getBrandingSettings().getChannel();

        //태그 파싱
        String keyword = channelSettings.getKeywords();
        List<Tag> parsedTagList = YoutubeAPIUtility.getParsedTag(keyword,description);
        idol.setTagList(parsedTagList);
        return idol;
    }

    public void setChannelThumbnailURL(String url)
    {
        this.channelThumbnailURL = url;
    }

    private void setChannelUrl(String channelURL)
    {
        this.channelUrl = channelURL;
    }

    private void setChannelId(String id)
    {
        this.channelId = id;
    }

    public void updateExistIdol(BigInteger subscriberCount, BigInteger viewCount, BigInteger videoCount,String channelThumbnailURL)
    {
        this.subscriberCount = subscriberCount;
        this.viewCount = viewCount;
        this.videoCount = videoCount;
        this.channelThumbnailURL = channelThumbnailURL;
    }

    public void setIdolToday(IdolToday idolToday) {
        this.idolToday = idolToday;
        this.idolToday.setIdol(this);
    }

    public void setIsBlocked(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public void addIdolToday()
    {
        IdolToday newIdolToday = new IdolToday();
        setIdolToday(newIdolToday);
    }


    //맨앞에서 저장함
    public void setIdolVideoInfo(IdolVideoInfo idolVideoInfo) {
        this.idolVideoInfoList.add(0,idolVideoInfo);
    }


    public CachedIdolDto toCachedIdolDto() {
        List<IdolVideoInfoDto> idolVideoInfoDtoList = idolVideoInfoList.stream()
                .map(IdolVideoInfoDto::fromEntity) // 각 엔티티를 DTO로 변환
                .collect(Collectors.toList()); // 리스트로 수집
        return CachedIdolDto.builder()
                .id(this.id)
                .channelName(this.channelName)
                .channelId(this.channelId)
                .subscriberCount(this.subscriberCount)
                .viewCount(this.viewCount)
                .videoCount(this.videoCount)
                .channelUrl(this.channelUrl)
                .channelThumbnailURL(this.channelThumbnailURL)
                .country(this.country)
                .isBlocked(this.isBlocked)
                .publishedAt(this.publishedAt)
                .description(this.description)
                .idolToday(IdolTodayDto.fromEntity(idolToday)) // IdolToday 변환 필요 시 추가 변환 메소드 사용
                .idolVideoInfoList(idolVideoInfoDtoList) // 필요에 따라 변환할 수도 있음
                .idolSubscriberTraceList(IdolSubscriberTraceDto.fromEntityList(this.idolSubscriberTraceList)) // 필요에 따라 변환할 수도 있음
                .build();
    }


}

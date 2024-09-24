package com.aiddoru.dev.DTO.Statistic;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.math.BigInteger;

public class IdolTodayResponseDto {

    //전날대비 구독자수
    private BigInteger subscribeDiff = BigInteger.valueOf(0);

    //전날대비 구독자수 퍼센테이지
    private Double subscribeDiffPercentage = 0.0;

    //오늘 클릭된 횟수
    private Long todayClicked = 0L;

    private Long subscribeCount = 0L;

    private String communityURL = null;
    private String channelName = null;
    private String thumbnailURL = null;

}



package com.aiddoru.dev.Controller.Statistic;


import com.aiddoru.dev.DTO.Statistic.AiddoruStatisticResponseDto;
import com.aiddoru.dev.DTO.Statistic.CommunityStatisticResponseDto;
import com.aiddoru.dev.DTO.Community.ThreadStatisticDto;
import com.aiddoru.dev.Service.Community.CommunityStatisticService;
import com.aiddoru.dev.Service.Rank.IdolStatisticService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/statistic")
@RequiredArgsConstructor
@Validated
public class StatisticAPIController
{
    private final CommunityStatisticService communityStatisticService;
    private final IdolStatisticService idolStatisticService;

    //얘도 10개만 리턴. 프론트에선 3개 자르고 더보기로 보여주기
    @GetMapping("/communityStatistic")
    public List<CommunityStatisticResponseDto> getCommunityStatistic(){
        return communityStatisticService.getCommunityStatisticList();
    }

    @GetMapping("/popularThread")
    public List<ThreadStatisticDto> getPopularThreadList(@RequestParam(defaultValue = "false",required = false) boolean isVisitOrder){
        List<ThreadStatisticDto> list = new ArrayList<>();
        list = communityStatisticService.getPopularThreadList(isVisitOrder);
        return list;
    }

    @GetMapping("/aiddoruStatistic")
    public List<AiddoruStatisticResponseDto> getIdolStatistic(){
        return idolStatisticService.getIdolStatisticList();
    }


}

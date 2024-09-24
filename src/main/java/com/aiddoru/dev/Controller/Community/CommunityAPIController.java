package com.aiddoru.dev.Controller.Community;

import com.aiddoru.dev.DTO.Community.CommunityResponseDto;
import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Service.Community.CommunityService;
import com.aiddoru.dev.Service.Community.ThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/community")
@RequiredArgsConstructor
@Validated
public class CommunityAPIController
{
    private final CommunityService communityService;
    private final ThreadService threadService;

    @GetMapping("/list")
    public List<CommunityResponseDto> getCommunityList(@RequestParam(required = false,defaultValue = "") String country) {
        List<CommunityResponseDto> responseDtoList = new ArrayList<>();
        List<Community> communityList = communityService.getCommunityList(country);

        for(Community community : communityList){
            responseDtoList.add(Community.convertToCommunityResponseDto(community,new ArrayList<>()));
        }


        return responseDtoList;
    }

    @GetMapping("/{communityName}")
    public CommunityResponseDto getCommunity(@PathVariable String communityName,
                                             @RequestParam(required = false,defaultValue = "1") long page,
                                             @RequestParam(required = false) String searchParam,
                                             @RequestParam(required = false) String searchKeyword,
                                             @RequestParam(required = false) String isRecommended){
        Community community = communityService.getCommunityByCommunityName(communityName);

        communityService.updateIdolTodayClick(community);

        List<Thread> pagedThreadList;
        long communityThreadCount;
        if(isRecommended != null){
            communityThreadCount = threadService.getRecommendedTotalPage(community.getId());
            pagedThreadList = threadService.getRecommendedThreadListByCommunityId(community.getId(),page);
        }else{
            communityThreadCount = threadService.getTotalPage(community.getId());
            pagedThreadList = threadService.getThreadListByCommunityId(community.getId(),page);
        }

        CommunityResponseDto communityResponseDto = Community.convertToCommunityResponseDto(community,pagedThreadList);
        communityResponseDto.setTotalPage(communityThreadCount);
        return communityResponseDto;
    }


}

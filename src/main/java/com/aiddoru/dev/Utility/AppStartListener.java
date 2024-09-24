package com.aiddoru.dev.Utility;

import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Service.Community.CommunityService;
import com.aiddoru.dev.Service.Rank.IdolVideoInfoService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@AllArgsConstructor
public class AppStartListener implements ApplicationListener<ApplicationReadyEvent> {

    private final CommunityService communityService;
    private final IdolVideoInfoService idolVideoInfoService;
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        try{
            Community community = Community.builder()
                    .communityName("freeBoard")
                    .communityURL("freeBoard")
                    .communityIdol(null)
                    .isBlocked(false)
                    .threadList(new ArrayList<>())
                    .idolName("")
                    .build();
            communityService.addCommunity(community, null);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

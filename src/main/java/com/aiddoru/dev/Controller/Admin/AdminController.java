package com.aiddoru.dev.Controller.Admin;

import com.aiddoru.dev.DTO.Auth.BlockUserRequestDto;

import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Service.Community.CommunityService;
import com.aiddoru.dev.Service.Community.ThreadService;
import com.aiddoru.dev.Service.Rank.*;
import com.aiddoru.dev.Service.User.AuthService;
import com.aiddoru.dev.Service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/admin")
public class AdminController {
    private final CommunityService communityService;

    private final UserService userService;

    private final AuthService authService;

    private final ThreadService threadService;

    private final IdolService idolService;

    private final IdolStatisticService idolStatisticService;
    private final RankService rankService;
    private final IdolVideoInfoService idolVideoInfoService;


    @PostMapping("/init")
    public void init() {
        Community community = Community.builder()
                .communityName("freeBoard")
                .communityURL("freeBoard")
                .communityIdol(null)
                .isBlocked(false)
                .threadList(new ArrayList<>())
                .idolName("")
                .build();


        communityService.addCommunity(community, null);
    }


    //getUserList 어드민 기능중에 누구 차단하거나 할때 쓰이는 기능임
    @GetMapping("/getUserList")
    public List<User> getUserList(
            @RequestParam(required = false,defaultValue = "1") long page,
            @RequestParam(required = false,defaultValue = "") String searchParam
    ) {
        return userService.getUserList(page,searchParam);
    }


    //커뮤니티 삭제 리스트.. 이거 쓰긴함?
    @DeleteMapping("/deleteList")
    public ResponseEntity<Void> deleteList(List<Long> deleteIdxList) {
        communityService.deleteList(deleteIdxList);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/modify")
    public ResponseEntity<Void> modifyIdol(@RequestBody Idol[] idolList) {
        for (Idol idol : idolList) {
            idolService.modifyIdol(idol);
        }

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteIdol(@RequestBody Long[] idList) {
        for (Long id : idList) {
            idolService.deleteIdol(id);
        }
        return ResponseEntity.ok().build();
    }

    @PutMapping("/block")
    public ResponseEntity<Void> blockUser(@RequestBody BlockUserRequestDto blockUserRequestDto){
        userService.block(blockUserRequestDto.getBlockUserId(),blockUserRequestDto.getBlockDay());
        return ResponseEntity.ok().build();
    }

    /**
     * 자유게시판이 아니라면 무조건 idol 있어야함
     * @return
     */
//    @PostMapping("/community")
//    public ResponseEntity<Void> addCommunity(@RequestBody Community community){
//        communityService.addCommunity(community, idol);
//        return ResponseEntity.ok().build();
//    }


    @PostMapping("/initPopularCommunity")
    public ResponseEntity<Void> setPopularCommunity(){
        System.out.println("init popular community");
        idolStatisticService.makePopularIdolCommunity();
        return ResponseEntity.ok().build();
    }


    //aiddoru의 최근 구독자 상승세
    @PutMapping("/calRecentFamous")
    public ResponseEntity<Void> calRecentFamous(){
        rankService.updateIdolCountProperties();
        return ResponseEntity.ok().build();
    }

    @GetMapping("/handleRecentVideo")
    public ResponseEntity<?> handleRecentVideo()
    {


        List<Idol> top50IdolList = idolService.getVTuberListOrderByRanking();
        List<String> top50ChannelIdList = new ArrayList<>();
        for(Idol idol : top50IdolList.subList(0,50)){
            top50ChannelIdList.add(idol.getChannelId());
        }
        idolVideoInfoService.updateRecentVideo(top50ChannelIdList);
        return ResponseEntity.ok().build();
    }


}

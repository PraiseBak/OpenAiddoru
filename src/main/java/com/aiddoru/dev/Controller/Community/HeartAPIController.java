package com.aiddoru.dev.Controller.Community;

import com.aiddoru.dev.Service.Community.HeartService;
import com.aiddoru.dev.Service.Community.RecommendService;
import com.aiddoru.dev.Utility.Security.ClientUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/heart")
public class HeartAPIController
{
    private final HeartService heartService;
    private final RecommendService recommendService;

    @PostMapping("/{communityName}/{commentId}/comment")
    //ip가져오는 부분
    public ResponseEntity<Void> commentHeartSet(@PathVariable String communityName, @PathVariable("commentId") long commentId, HttpServletRequest request
                ,@RequestBody Map<String, Boolean> requestBody){
        String ip = ClientUtils.getRemoteIP(request);
        boolean isPositive = requestBody.get("isPositive"); // Extract isPositive from the JSON data
        heartService.commentHeartSet(commentId,ip,isPositive);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{communityName}/{threadId}")
    @Transactional
    //ip가져오는 부분
    public ResponseEntity<Void> heartSet(@PathVariable String communityName, @PathVariable("threadId") long threadId, HttpServletRequest request
               ,@RequestBody Map<String, Boolean> requestBody // Use @RequestBody to receive JSON data
    ){
        String ip = ClientUtils.getRemoteIP(request);
        boolean isPositive = requestBody.get("isPositive"); // Extract isPositive from the JSON data
        long heartCount = heartService.heartSet(threadId,ip,isPositive);
        recommendService.updateRecommend(heartCount,threadId,communityName);
        return ResponseEntity.ok().build();
    }






    //추천취소 필요없음
//    @DeleteMapping("/{communityName}/{threadId}")
//    public void heartCancel(@PathVariable long threadId, HttpServletRequest request, boolean isPositive,@PathVariable String communityName){
//        String ip = ClientUtils.getRemoteIP(request);
//        heartService.heartCancel(threadId,ip);
//    }
}

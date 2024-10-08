package com.aiddoru.dev.Controller.Community;

import com.aiddoru.dev.DTO.Community.ThreadResponseDto;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Service.Community.ThreadService;
import com.aiddoru.dev.Utility.Security.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/thread")
@RequiredArgsConstructor
public class ThreadAPIController
{
    private final ThreadService threadService;

    private final SecurityUtil securityUtil;

    @GetMapping("/{communityName}/{threadId}")
    public ThreadResponseDto getThread(@PathVariable String communityName,
                                     @PathVariable String threadId){

        threadService.addVisitCount(Long.parseLong(threadId));
        return threadService.
                getThread(Long.parseLong(threadId)).toThreadResponseDto();
    }

    @PostMapping("/{communityURL}/thread")
    public void addThread(@Valid @RequestBody Thread thread,
                          @PathVariable String communityURL,
                          HttpServletRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = securityUtil.getUsernameFromRequest(request);
        threadService.addThread(communityURL,username,thread);
    }

    @DeleteMapping("/{communityName}/{threadId}")
    public void deleteThread(@PathVariable String communityName,@PathVariable Long threadId, HttpServletRequest request){
        String username = securityUtil.getUsernameFromRequest(request);
        threadService.deleteThread(threadId,username);
    }


    @PutMapping("/{communityName}/{threadId}")
    void modifyThread(@PathVariable String communityName,@PathVariable Long threadId, HttpServletRequest request
            ,@Valid @RequestBody Thread thread){
        String username = securityUtil.getUsernameFromRequest(request);
        threadService.modifyThread(username,threadId,thread);
    }


    @GetMapping("/{communityName}/recommend")
    public List<ThreadResponseDto> getRecommendThreadList(@PathVariable String communityName, @RequestParam(defaultValue = "1") Long page){
        return threadService.getRecommendedThreadList(page);
    }


}

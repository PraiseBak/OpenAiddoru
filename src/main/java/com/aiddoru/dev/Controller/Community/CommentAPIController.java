package com.aiddoru.dev.Controller.Community;


import com.aiddoru.dev.Domain.Entity.Community.Comment;
import com.aiddoru.dev.Service.Community.CommentService;
import com.aiddoru.dev.Utility.Security.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/comment")
@RestController
@RequiredArgsConstructor
public class CommentAPIController
{
    private final CommentService commentService;
    private final SecurityUtil securityUtil;

    @GetMapping("/{communityName}/{threadId}/{commentId}")
    Comment getComment(@PathVariable long commentId, @PathVariable String communityName, @PathVariable String threadId){
        return commentService.getComment(commentId);
    }

    @PostMapping("/{communityName}/{threadId}")
    ResponseEntity<Void> addComment(@Valid @RequestBody Comment comment,
                              @RequestParam(name = "ownerCommentId",required = false) Long ownerCommentId,
                              @PathVariable String communityName, @PathVariable long threadId, HttpServletRequest request){
        String username = securityUtil.getUsernameFromRequest(request);

        if(ownerCommentId == null){
            commentService.addComment(threadId,comment,username);
        }else{
            commentService.addSubComment(ownerCommentId,comment,username);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{communityName}/{threadId}/{commentId}")
    ResponseEntity<Void> deleteComment(@PathVariable long threadId,@PathVariable long commentId,@PathVariable String communityName, HttpServletRequest request){
        String username = securityUtil.getUsernameFromRequest(request);
        commentService.deleteComment(username,commentId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{communityName}/{threadId}/{commentId}")
    ResponseEntity<Void> modifyComment(@PathVariable long threadId,@PathVariable long commentId,@PathVariable String communityName, HttpServletRequest request
            ,@Valid @RequestBody  Comment comment){
        String username = securityUtil.getUsernameFromRequest(request);
        commentService.modifyComment(commentId,comment,username);
        
        
        return ResponseEntity.ok().build();
    }

}

package com.aiddoru.dev.Service.Community;


import com.aiddoru.dev.Domain.Entity.Community.Comment;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Persistence.Community.CommentRepository;
import com.aiddoru.dev.Service.User.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService
{
    private final UserService userService;
    private final ThreadService threadService;
    private final CommentRepository commentRepository;


    @Transactional
    public void addComment(long threadId, Comment comment, String username)
    {
        User user = userService.getUserByUsername(username);
        comment.setUser(user);

        Thread thread = threadService.getThread(threadId);
        thread.addComment(comment);
        commentRepository.save(comment);
    }

    @Transactional
    public void addSubComment(long ownerCommentId, Comment subComment, String username) {

        User user = userService.getUserByUsername(username);
        user.addComment(subComment);

        Comment ownerComment = getComment(ownerCommentId);

        //subComment에는 subComment가 추가로 붙을 수 없음.
        if(ownerComment.isSubComment()){
            throw new CustomException(CustomErrorCode.INVALID_RESOURCE);
        }

        ownerComment.addSubComment(subComment);
        subComment.setIsSubComment(true);



        Thread ownerCommentThread = ownerComment.getThread();
        ownerCommentThread.addComment(subComment);

        // 대댓글을 저장
        commentRepository.save(subComment);
    }

    @Transactional
    public Comment getComment(long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
    }

    @Transactional
    public void deleteComment(String username, long commentId) {
        Comment deleteComment = findCommentByUsername(username,commentId);

        deleteComment.getThread().getCommentList().remove(deleteComment);
        commentRepository.delete(deleteComment);
    }

    private Comment findCommentByUsername(String username, long commentId){
        Optional<Comment> comment = commentRepository.findById(commentId);
        if(comment.isPresent() && comment.get().getUser().getUsername().equals(username)){
            return comment.get();
        }
        throw new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND);
    }


    @Transactional
    public void modifyComment(long commentId, Comment comment, String username) {
        Comment modifyComment = findCommentByUsername(username,commentId);
        modifyComment.setContent(comment.getContent());
    }

    public long getCommentCount(Thread thread) {
        return thread.getCommentList().size();
    }
}

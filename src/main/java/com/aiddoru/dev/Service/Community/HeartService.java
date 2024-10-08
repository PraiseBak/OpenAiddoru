package com.aiddoru.dev.Service.Community;

import com.aiddoru.dev.Domain.Entity.Community.Comment;
import com.aiddoru.dev.Domain.Entity.Community.Heart;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Persistence.Community.HeartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService
{
    private final ThreadService threadService;
    private final HeartRepository heartRepository;
    private final CommentService commentService;


    //이미 중복된 추천인지 확인
    boolean isDuplicateHeart(Thread thread, String ip, boolean isPositive){
        return thread.getHeartList().stream().anyMatch((t) -> t.getIp().equals(ip) && t.isPositive() == isPositive);
    }

    //인터페이스 만들어서 하나로 처리하는게 나을까?
    boolean isDuplicateHeart(Comment comment, String ip, boolean isPositive){
        return comment.getHeartList().stream().anyMatch((t) -> t.getIp().equals(ip) && t.isPositive() == isPositive);
    }


    /*

    return added heart count
     */
    @Transactional
    public long heartSet(long threadId, String ip,boolean isPositive)
    {
        Thread thread = threadService.getThread(threadId);
        if(isDuplicateHeart(thread,ip,isPositive)) throw new CustomException(CustomErrorCode.DUPLICATE_RESOURCE);
        Heart heart = Heart.builder()
                .ip(ip)
                .isPositive(isPositive)
                .build();
        thread.getHeartList().add(heart);
        thread.getThreadStatistic().addTodayHeartCount();
        heartRepository.save(heart);
        long heartCount = thread.getHeartList().stream().filter(Heart::isPositive).count();

        return heartCount;
    }



    //deprecated
    @Transactional
    public void heartCancel(long threadId, String ip) {
        Thread thread = threadService.getThread(threadId);
        Heart findHeart = thread.getHeartList()
                .stream()
                .filter(heart -> heart.getIp().equals(ip))
                .findFirst()
                .orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
        thread.getHeartList().remove(findHeart);
    }

    //댓글 추천
    @Transactional
    public void commentHeartSet(long commentId, String ip,boolean isPositive)
    {
        Comment comment = commentService.getComment(commentId);
        if(isDuplicateHeart(comment,ip,isPositive)) throw new CustomException(CustomErrorCode.DUPLICATE_RESOURCE);
        Heart heart = Heart.builder()
                .ip(ip)
                .isPositive(isPositive)
                .build();
        comment.getHeartList().add(heart);
        heartRepository.save(heart);
    }
}

package com.aiddoru.dev.Service.Community;

import com.aiddoru.dev.DTO.Community.ThreadResponseDto;
import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Domain.Entity.Community.ThreadStatistic;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Persistence.Community.ThreadRepository;
import com.aiddoru.dev.Persistence.Community.ThreadStatisticRepository;
import com.aiddoru.dev.Service.User.UserService;
import com.aiddoru.dev.Utility.Page.PageUtility;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThreadService
{

    private final ThreadRepository threadRepository;

    private final UserService userService;

    private final CommunityService communityService;
    private final PageUtility pageUtility;
    private final ThreadStatisticRepository threadStatisticRepository;

    @Transactional
    public void addThread(String communityName, String username, Thread thread)
    {
        User writeUser = userService.getUserByUsername(username);
        Community community = communityService.getCommunityByName(communityName);
        thread.setUser(writeUser);
        community.addThread(thread);
        community.getCommunityStatistic().addTodayThreadCount();
        thread.setCommunity(community);

        ThreadStatistic threadStatistic = new ThreadStatistic();
        threadStatisticRepository.save(threadStatistic);
        thread.setThreadStatistic(threadStatistic);
    }

    //communityName까지 체크해야하나 싶어서 삭제함
    @Transactional
    public void deleteThread(Long threadId, String username)
    {
        Thread thread = getThread(threadId);
        if(thread.getUser().getUsername().equals(username)){
            thread.setDelete();
        }
    }

    public Thread getThread(long threadId)
    {
        Optional<Thread> thread = threadRepository.findByIdAndIsDeleted(threadId,false);
        return thread.orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
    }


    @Transactional
    public void modifyThread(String username, Long threadId, Thread thread) {
        Thread modifyThread = getThread(threadId);
        if(modifyThread.getUser().getUsername().equals(username)){
            modifyThread.setTitle(thread.getTitle());
            modifyThread.setContent(thread.getContent());
        }
    }

    public List<ThreadResponseDto> getRecommendedThreadList(long page) {
        Pageable pageable = pageUtility.getPageable(page);
        List<Thread> pagedThreadList = threadRepository.findByIsRecommendedOrderByCreatedDateDesc(pageable,true);
        List<ThreadResponseDto> pagedThreadResponseDtoList = new ArrayList<>();
        for(Thread thread : pagedThreadList){
            pagedThreadResponseDtoList.add(thread.toThreadResponseDto());
        }
        return pagedThreadResponseDtoList;
    }

    public List<ThreadResponseDto> getThreadList(long page) {
        Pageable pageable = pageUtility.getPageable(page);
        List<Thread> pagedThreadList = threadRepository.findAllByOrderByCreatedDateDesc(pageable);
        List<ThreadResponseDto> pagedThreadResponseDtoList = new ArrayList<>();
        for(Thread thread : pagedThreadList){
            pagedThreadResponseDtoList.add(thread.toThreadResponseDto());
        }

        return pagedThreadResponseDtoList;
    }

    public List<Thread> getThreadListByCommunityId(Long communityId, long page) {
        Pageable pageable = pageUtility.getPageable(page);
        return threadRepository.findByCommunity_IdAndIsDeletedFalseOrderByCreatedDateDesc(communityId,pageable);
    }

    public long getTotalPage(long communityId){
        long threadCount = threadRepository.countThreadsByCommunityId(communityId);
        return (threadCount / PageUtility.perSize) + 1L;
    }


    @Transactional
    public void addVisitCount(Long threadId) {
        Thread thread = threadRepository.findByIdAndIsDeleted(threadId,false).orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
        thread.addVisitCount();
    }

    public List<Thread> getRecommendedThreadListByCommunityId(Long communityId, long page) {
        Pageable pageable = pageUtility.getPageable(page);
        return threadRepository.findByCommunity_IdAndIsDeletedFalseAndIsRecommendedOrderByCreatedDateDesc(communityId,pageable,true);
    }

    public long getRecommendedTotalPage(Long communityId) {
        long threadCount = threadRepository.countThreadsByCommunityIdAndIsRecommended(communityId,true);
        return (threadCount / PageUtility.perSize) + 1L;


    }
}

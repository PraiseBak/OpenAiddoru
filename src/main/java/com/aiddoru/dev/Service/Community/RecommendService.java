package com.aiddoru.dev.Service.Community;

import com.aiddoru.dev.Domain.Entity.Community.CommunityRecommend;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Persistence.Community.CommunityRecommendRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecommendService
{
    private final ThreadService threadService;
    private final CommunityRecommendRepository communityRecommendRepository;
    private final CommentService commentService;

    /*
        추천 요청이 들어왔을때
        추천글 갱신
     */
    public void updateRecommend(long heartCount, long threadId, String communityName)
    {
        Thread thread = threadService.getThread(threadId);
        CommunityRecommend communityRecommend = communityRecommendRepository.findByCommunityName(communityName);

        long recommendStandard = communityRecommend.getRecommendStandard();
        communityRecommend.addRecommendedHeartCount();

        //heart 기준충족 && comment 개수 추천의 1/4
        long commentCount = commentService.getCommentCount(thread);
        if(heartCount >= recommendStandard && commentCount >= (heartCount / 4)){
            thread.setRecommend();
            communityRecommend.addRecommendedThreadCount();
        }

        updateRecommendStandard(communityRecommend);
    }

    /*
    념글 기준 업데이트
     */
    @Transactional
    public void updateRecommendStandard(CommunityRecommend communityRecommend) {
        if(communityRecommend.getRecommendedThreadCount() != 0){
            int averageHeart = (int)(communityRecommend.getRecommendedHeartCount() / communityRecommend.getRecommendedThreadCount());
            int newStandard = 0;
            if(averageHeart >= 10){
                int decreaseMagicNum = (int) (communityRecommend.getRecommendedThreadCount() / 30);
                //표준화했을때 10보다 작으면 average 값으로, 10보다 크다면 표준화된 값을 적용한다
                newStandard = averageHeart - decreaseMagicNum < 10 ? averageHeart : (averageHeart - decreaseMagicNum);
                communityRecommend.setRecommendStandard(newStandard);
            }
        }
    }


    /*
    념글 조회
     */

    private CommunityRecommend getCommunityRecommend(String communityName) {
        CommunityRecommend communityRecommend = communityRecommendRepository.findByCommunityName(communityName);
        return communityRecommend;

    }


}



package com.aiddoru.dev.Service.Community;

import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Domain.Entity.Community.CommunityRecommend;
import com.aiddoru.dev.Domain.Entity.Community.CommunityStatistic;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Persistence.Community.CommunityRecommendRepository;
import com.aiddoru.dev.Persistence.Community.CommunityRepository;
import com.aiddoru.dev.Persistence.Community.CommunityStatisticRepository;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommunityService
{
    private final CommunityRepository communityRepository;
    private final CommunityStatisticRepository communityStatisticRepository;
    private final IdolRepository idolRepository;
    private final CommunityRecommendRepository communityRecommendRepository;

    public Community getCommunityByCommunityName(String communityName)
    {
        //지금까지 communityName으로 받은줄알았던게 communityURL이었음
        Community community = communityRepository.findByCommunityURL(communityName).orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
        return community;
    }

    public List<Community> getCommunityList(String country)
    {
        if(country == null || country.isEmpty()){
            return communityRepository.findAll();
        }
        return communityRepository.findByCommunityIdolCountry(country);

    }


    public Community getCommunityByName(String communityName)
    {
        System.out.println(communityName);
        return communityRepository.findByCommunityURL(communityName).orElseThrow(() -> new CustomException(CustomErrorCode.RESOURCE_NOT_FOUND));
    }

    @Transactional
    public void addCommunity(Community community, Idol idol)
    {
        if(communityRepository.findByCommunityNameOrCommunityURL(community.getCommunityName(),community.getCommunityURL()).isEmpty()){
            CommunityStatistic communityStatistic = new CommunityStatistic();

            communityStatisticRepository.save(communityStatistic);

            community.setCommunityStatistic(communityStatistic);
            Community savedCommunity;
            try{
                savedCommunity = communityRepository.save(community);
                savedCommunity.setCommunityStatistic(communityStatistic);
                communityStatistic.setCommunity(savedCommunity);
            }catch (RuntimeException runtimeException){
                log.error("community 자동생성 에러 발생 = " + runtimeException.getMessage());
                return;
            }

            //커뮤니티의 추천기준을 나타내는 엔티티
            CommunityRecommend communityRecommend = new CommunityRecommend();
            communityRecommend.setCommunityName(community.getCommunityName());
            communityRecommendRepository.save(communityRecommend);

            savedCommunity.setIdol(idol);
        }
    }

//    // TODO: 2023-10-19  무조건 삭제해야함
//    @Transactional
//    public void onlyThisTime()
//    {
//        for(Community community : communityRepository.findAll()){
//            if(community.getCommunityStatistic() == null){
//                CommunityStatistic newCommunityStatistic = new CommunityStatistic();
//                community.setCommunityStatistic(newCommunityStatistic);
//            }
//
//        }
//    }

    public void deleteList(List<Long> deleteIdxList) {
        for(Long i : deleteIdxList){
            communityRepository.deleteById(i);
        }
    }

    public void updateIdolTodayClick(Community community) {
        if(community.getCommunityIdol() != null){
            community.getCommunityIdol().getIdolToday().clickUpdate();
        }
    }

    public boolean isExistsCommunityName(String communityName) {
        return communityRepository.findByCommunityNameOrCommunityURL(communityName,communityName).isPresent();
    }
}


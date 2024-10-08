package com.aiddoru.dev.Service.Rank;

import com.aiddoru.dev.DTO.Statistic.AiddoruStatisticResponseDto;
import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Rank.IdolToday;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Persistence.Rank.IdolTodayRepository;
import com.aiddoru.dev.Service.Community.CommunityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class IdolStatisticService {


    private final IdolTodayRepository idolTodayRepository;
    private final IdolRepository idolRepository;
    private final CommunityService communityService;

    public List<AiddoruStatisticResponseDto> getIdolStatisticList() {
        List<AiddoruStatisticResponseDto> idolStatisticReponseDtoList = new ArrayList<>();
        //today clicked가 0이아닌 ad만 출력


        for(IdolToday idolToday : idolTodayRepository.findTop10ByTodayClickedNotOrderByTodayClicked(0L)){
            idolStatisticReponseDtoList.add(AiddoruStatisticResponseDto.builder()
                            .channelName(idolToday.getIdol().getChannelName())
                            .idolToday(idolToday)
                            .idolVideoInfoList(idolToday.getIdol().getIdolVideoInfoList())
                            .channelUrl(idolToday.getIdol().getChannelUrl())
                            .channelThumbnailURL(idolToday.getIdol().getChannelThumbnailURL())
                            .build());

        }
        return idolStatisticReponseDtoList;
    }


    //주기적으로 커뮤니티 갱신
    @Scheduled(cron = "30 46 15 * * *")
    public void makePopularIdolCommunity(){
        String[] countryCodeList = {"KR","US","JP"};
        //국가별로 생성
        for(String countryCode: countryCodeList){
            List<Idol> idolList = idolRepository.findTop30ByCountryOrderBySubscriberCountDesc(countryCode);

            for(Idol idol: idolList){
                String communityName = idol.getChannelName();
                if(communityService.isExistsCommunityName(communityName)){
                    continue;
                }
                try{
                    Community newCommunity = Community.builder()
                            .communityIdol(idol)
                            .communityName(communityName)
                            .communityURL(communityName)
                            .idolName(idol.getChannelName())
                            .build();
                    communityService.addCommunity(newCommunity,idol);
                }catch (RuntimeException runtimeException){
                    log.error(String.format("community 생성 중 에러 발생 = ",runtimeException.getMessage()));
                    
                }
            }
        }
    }


    //채널이름을 커뮤니티 이름으로 변환
    private String parseChannelNameToCommunityName(String channelName){
        String firstToken = channelName.split(" ")[0];
        return firstToken;
    }


}

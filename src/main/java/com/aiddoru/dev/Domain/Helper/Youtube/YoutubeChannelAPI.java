package com.aiddoru.dev.Domain.Helper.Youtube;

import com.aiddoru.dev.Utility.YoutubeAPI.YoutubeAPIUtility;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeChannelAPI {


    @Value("${youtube.api.key}")
    private String API_KEY;

    private final YouTube.Channels.List channelRequest;

    private YoutubeSearchAPI youtubeSearchAPI;
    private YoutubeAPIUtility youtubeAPIUtility;

    private static final int CHANNEL_MAX = 50;

    @Autowired
    public YoutubeChannelAPI(YoutubeAPI youtubeAPI,YoutubeSearchAPI youtubeSearchAPI,YoutubeAPIUtility youtubeAPIUtility) throws GeneralSecurityException, IOException {
        this.youtubeAPIUtility = youtubeAPIUtility;
        this.youtubeSearchAPI = youtubeSearchAPI;
        YouTube youtube = youtubeAPI.getService();
        channelRequest = youtube.channels().list("statistics,snippet,brandingSettings");
    }

    @PostConstruct
    public void keyInit() {
        channelRequest.setKey(API_KEY);
        channelRequest.setMaxResults(50L);  // 가져올 채널 수 설정
    }

//    public List<Channel> getChannelFromChannelIdList(String channelIdList) {
//        List<Channel> channelRequestResultList = new ArrayList<>();
//        StringBuilder idsStringBuilder = new StringBuilder();
//        int channelCount = 0;
//
//        for (String channelId : channelIdList) {
//
//            channelParamAdd(channelId,idsStringBuilder);
//            //todo
//            //요청보내고 페이징요청을 50개밖에 못하는거지 해당 ids 가져오는걸 50개밖에 못하는건 아니지 않나?
//            //테스트
//            channelCount++;
//            if (channelCount == CHANNEL_MAX) {
//                channelRequestResultList.addAll(sendChannelRequestList(idsStringBuilder));
//                channelCount = 0;
//            }
//        }
//
//        //50개가 아닌 잔여채널이 남아있는 경우
//        if(channelCount != 0){
//            channelRequestResultList.addAll(sendChannelRequestList(idsStringBuilder));
//        }
//        return channelRequestResultList;
//    }


    @Transactional
    public List<Channel> getChannelListFromChannelIdsQuery(String queryStr) {
        return sendChannelListRequest(queryStr);
    }

    /*
    50개 단위로 요청을 보냄
     */
     private List<Channel> sendChannelListRequest(String query) {
        try{
            setChannelRequestParams(query);
            return new ArrayList<>(channelRequest.execute().getItems());
        }
        //예외처리
        catch (IOException ioException){
            ioException.printStackTrace();
            System.out.println("getChannelFromCHannelIdList에서 ioException 발생");
        }
        return null;
    }


    private void setChannelRequestParams(String param) {
        channelRequest.setId(param);
    }

}

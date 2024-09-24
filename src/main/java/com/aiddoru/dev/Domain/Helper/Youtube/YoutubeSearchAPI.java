package com.aiddoru.dev.Domain.Helper.Youtube;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Domain.Enum.ChannelConstEnum;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Persistence.Rank.IdolVideoInfoRepository;
import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.SearchResultSnippet;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class YoutubeSearchAPI {
    @Value("${youtube.api.key}")
    private String API_KEY;
    private YouTube.Search.List searchRequest;
    private IdolRepository idolRepository;
    private IdolVideoInfoRepository idolVideoInfoRepository;

    @Autowired
    public YoutubeSearchAPI(IdolRepository idolRepository,IdolVideoInfoRepository idolVideoInfoRepository) throws GeneralSecurityException, IOException {
        YoutubeAPI youtubeAPI = new YoutubeAPI();
        YouTube youTube = youtubeAPI.getService();
        searchRequest = youTube.search().list("snippet");
        searchRequest.setKey(API_KEY);
        qInit();
        searchRequest.setType("channel");
        searchRequest.setOrder("viewCount");
        searchRequest.setMaxResults(50L);  // 가져올 채널 수 설정
        this.idolRepository = idolRepository;
        this.idolVideoInfoRepository = idolVideoInfoRepository;
    }

    @PostConstruct
    public void keyInit() {
        searchRequest.setKey(API_KEY);
    }

    /**
     * VTuber에서 운동 유튜버로 변경해보자
     */
    private void qInit() {
        ChannelConstEnum channelConstEnum = ChannelConstEnum.VTuber;

        StringBuilder qStringBuilder = new StringBuilder();

        for (String q : channelConstEnum.getSearchKeyword()) {
            qStringBuilder.append(q).append(",");
        }

        qStringBuilder.deleteCharAt(qStringBuilder.lastIndexOf(","));
        searchRequest.setQ(qStringBuilder.toString());
        searchRequest.setOrder("viewCount");
    }


    //50 * 60 = 3000건 가져옴
    public List<String> getSearchChannelIdList() throws IOException {
        String nextToken = null;
        int nextTokenCount = 0;
        List<String> channelIdList = new ArrayList<>();

        try {
            do {
                SearchListResponse searchListResponse = searchRequest.execute();
                List<SearchResult> searchResults = searchListResponse.getItems();

                for (SearchResult searchResult : searchResults) {
                    channelIdList.add(searchResult.getSnippet().getChannelId());
                }
                nextToken = searchListResponse.getNextPageToken();
                searchRequest.setPageToken(nextToken);

                nextTokenCount++;
            } while (nextToken != null && nextTokenCount < 60);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.info(String.format("getSearchChannelIdList =>  get search size = %d",channelIdList.size()));

        return channelIdList;
    }

    public String getSearchChannelByHandelId(String channelId) {
        searchRequest.setQ(channelId);
        searchRequest.setOrder(null);
        try {
            SearchListResponse searchListResponse = searchRequest.execute();
            List<SearchResult> searchResults = searchListResponse.getItems();
            return searchResults.stream()
                    .findFirst()
                    .map(result -> result.getId().getChannelId())
                    .orElse(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        qInit();
        return null;
    }


    @Builder
    @Data
    static class PrevTypeForSearchRequest {
        String channelId;
        String prevType;
        Long maxResult;
        String order;
    }

    @Transactional
    public void updateVideoInfoByChannelIdList(List<String> channelIdList) {
        //search모드 미리 백업
        PrevTypeForSearchRequest prevTypeForSearchRequest = PrevTypeForSearchRequest.builder()
                .prevType(searchRequest.getType())
                .maxResult(searchRequest.getMaxResults())
                .order(searchRequest.getOrder())
                .build();

        //채널은 한개씩
        searchRequest.setType("video");
        searchRequest.setMaxResults(searchRequest.getMaxResults());
        searchRequest.setOrder("date");

        for (String channelId : channelIdList) {
            searchRequest.setChannelId(channelId);

            try {
                List<SearchResult> resultItems = searchRequest.execute().getItems();
                Optional<Idol> optionalIdol = idolRepository.findByChannelId(channelId);
                Idol idol = null;
                if (optionalIdol.isPresent()) idol = optionalIdol.get();
                else continue;

                //삭제하지않고 중복인지 확인하고 관리하도록 수정
                if(!resultItems.isEmpty()){
//                    idolVideoInfoRepository.deleteAll(idol.getIdolVideoInfoList());
                }

                for (SearchResult searchResult : resultItems) {
                    log.info(String.format("가져온 searchResult = %s",searchResult.toString()));
                    SearchResultSnippet snippet = searchResult.getSnippet();
                    String thumbnailURL = snippet.getThumbnails().getDefault().getUrl();
                    DateTime publishedAt = snippet.getPublishedAt();
                    String videoId = searchResult.getId().getVideoId();

                    //이미 존재하는 영상이면 스킵
                    if(idolVideoInfoRepository.existsByVideoThumbnailURL(thumbnailURL)) continue;
                    if(idolVideoInfoRepository.existsByVideoId(videoId)) continue;

                    // DateTime를 Instant로 변환
                    Instant instant = Instant.ofEpochMilli(publishedAt.getValue());

                    // Instant를 LocalDateTime로 변환
                    LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                    IdolVideoInfo idolVideoInfo = IdolVideoInfo.builder()
                            .publishedAt(localDateTime)
                            .videoThumbnailURL(thumbnailURL)
                            .videoId(videoId)
                            .build();
                    idol.setIdolVideoInfo(idolVideoInfo);
                }
            } catch (IOException e) {
                log.error("video가져오는데 실패하였습니다. error" + e.getMessage());
            }
        }

        //setting 원래대로 돌리기
        searchRequest.setOrder(prevTypeForSearchRequest.getOrder());
        searchRequest.setType(prevTypeForSearchRequest.getPrevType());
        searchRequest.setMaxResults(prevTypeForSearchRequest.getMaxResult());
    }



}



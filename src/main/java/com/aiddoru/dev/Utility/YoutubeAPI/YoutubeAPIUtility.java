package com.aiddoru.dev.Utility.YoutubeAPI;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeSearchAPI;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class YoutubeAPIUtility {

    private final YoutubeSearchAPI youtubeSearchAPI;

    private String getHandledIdToChannelId(String handledId){
        return youtubeSearchAPI.getSearchChannelByHandelId(handledId);
    }

    public List<String> listSplitByUnitNum(List<String> youtubeChannelIdList,int UNIT) {
        int i=0;

        List<String> splitedList = new ArrayList<>();
        StringBuilder stringBuilder = new StringBuilder();

        while(i != youtubeChannelIdList.size()) {
            String channelId = youtubeChannelIdList.get(i);

            if(channelId.startsWith("@")) {
                channelId = getHandledIdToChannelId(channelId);
                if(channelId == null){
                    i++;
                    continue;
                }
            }
            stringBuilder.append(channelId).append(",");

            if(youtubeChannelIdList.size()-1 == i){
                stringBuilder.deleteCharAt(stringBuilder.length()-1);
                splitedList.add(stringBuilder.toString());
                break;
            }

            //split 단위 충족
            if((i % UNIT == 0 && !stringBuilder.isEmpty())){
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
                splitedList.add(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }

            i++;
        }

        return splitedList;
    }


    public static List<Tag> getParsedTag(String string, String description) {
        if(string == null || string.isEmpty()) return new ArrayList<>();
        // 정규표현식: 따옴표로 묶인 문구는 유지하면서 특수문자 제거
        String regex = "\"[^\"]*\"|[\\p{L}\\p{N}]+";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        List<Tag> result = new ArrayList<>();
        while (matcher.find()) {
            String match = matcher.group().replaceAll("\"", "");
            Tag tag = Tag.builder()
                    //description에 태그가 있다면 중요한 정보이므로 가중치 3
                    .weight(description.contains(match) ? 3 : 1)
                    .tag(match)
                    .build();
            result.add(tag);
        }

        return result;
    }

}

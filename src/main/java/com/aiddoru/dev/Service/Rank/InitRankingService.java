package com.aiddoru.dev.Service.Rank;

import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeChannelAPI;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Persistence.Rank.IdolTodayRepository;
import com.aiddoru.dev.Utility.YoutubeAPI.YoutubeAPIUtility;
import com.google.api.services.youtube.model.Channel;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.core.io.UrlResource;


@Service
@RequiredArgsConstructor
@Slf4j
public class InitRankingService
{
    private final YoutubeChannelAPI youtubeChannelAPI;
    private final IdolRepository idolRepository;
    private final IdolTodayRepository idolTodayRepository;
    private final IdolService idolService;
    private final YoutubeAPIUtility youtubeAPIUtility;


    //이때 파일은 jar 외부 디렉토리에 저장됩니다
    private ArrayList<String> getIdListFromFile(){
        HashSet<String> set = new HashSet<>();
        try
        {
            // 할때마다 만듦
            makeInitIdListFileFromHTML();

            File inputFile = new File("./src/main/resources/static/text/InitIdList.txt");
            BufferedReader br = new BufferedReader(new FileReader(inputFile));

            String line;
            while((line = br.readLine()) != null){
                set.add(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        // HashSet을 ArrayList로 변환
        System.out.println("set size = " + set.size());
        return new ArrayList<>(set);
    }


    //초기 파일 없을때 한번만 실행
    public List<String> makeInitIdListFileFromHTML() {
        Set<String> set = new HashSet<>();
        try {
            ClassPathResource resource = new ClassPathResource("static/text/InitRankingHTML.txt");
            InputStream inputStream = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

            String line;
            while ((line = br.readLine()) != null) {
                // Regular expression to match both types of YouTube links
                String regex = "https://www\\.youtube\\.com/(?:@\\w+|channel/[A-Za-z0-9_-]+)";

                // Compile the regex pattern
                Pattern pattern = Pattern.compile(regex);
                // Find and print all matched YouTube links in the text
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String youtubeLink = matcher.group(0);
                    set.add(youtubeLink);
                }
            }
        } catch (IOException e) {
            return null;
        }

        HashSet<String> resultSet = new HashSet<>();
        for(String s : set){
            String[] tmp = s.split("/");
            String tmpStr = tmp[tmp.length-1];
            resultSet.add(tmpStr);
        }

        //파일 작성하는 부분
        BufferedWriter bw = null;
        try
        {
            File outputFile = new File("./src/main/resources/static/text/InitIdList.txt");
            if(outputFile.length() != 0){
                return null;
            }
            bw = new BufferedWriter(new FileWriter(outputFile));

            for (String s : resultSet)
            {
                bw.write(s + "\n");
            }
            bw.flush();
            bw.close();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        List<String> resultIdList = resultSet.stream().toList();
        return resultIdList;
    }



    //기존파일로부터 불러와서 갱신하는 함수
    public boolean setInitRanking()
    {
        if(idolRepository.count() != 0){
            log.info("이미 데이터가 존재하여 할당량을 절약하기 위해 실행하지 않습니다.");
            return false;
        }
        List<String> youtubeChannelIdList = getIdListFromFile();


        long count = youtubeChannelIdList.stream()
                .mapToLong(str -> str.chars().filter(ch -> ch == '@').count())
                .sum();

        log.info(String.valueOf(count));
        if(count != 0) return true;

        int UNIT = 50;
        List<String> channelIdsQueryList = youtubeAPIUtility.listSplitByUnitNum(youtubeChannelIdList,UNIT);
        log.info(channelIdsQueryList.stream().collect(Collectors.joining(",")));
        List<Channel> channelResultList;

        //50개마다 요청해서 이를 저장합니다
        for(String query: channelIdsQueryList){
            channelResultList = youtubeChannelAPI.getChannelListFromChannelIdsQuery(query);
            for (Channel channel : channelResultList) {
                idolService.saveIdol(channel);
            }
        }


        log.info(String.format("업데이트된 idol 수 = %d",youtubeChannelIdList.size()));

        log.info("set init ranking end");
        return true;

    }


}
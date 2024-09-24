package statistic;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeChannelAPI;
import com.aiddoru.dev.Domain.Helper.Youtube.YoutubeSearchAPI;
import com.aiddoru.dev.Persistence.Rank.IdolRepository;
import com.aiddoru.dev.Service.Rank.*;
import com.aiddoru.dev.Utility.YoutubeAPI.YoutubeAPIUtility;
import com.google.api.services.youtube.model.Channel;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@Service
public class TestRankService extends RankService{
    private IdolRepository idolRepository;
    private IdolTodayService idolTodayService;
    private YoutubeAPIUtility youtubeAPIUtility;



    public TestRankService(YoutubeChannelAPI youtubeChannelAPI, IdolService idolService, IdolRepository idolRepository, IdolTodayService idolTodayService, IdolVideoInfoService idolVideoInfoService, InitRankingService initRankingService, YoutubeAPIUtility youtubeAPIUtility) {
        super(youtubeChannelAPI,idolService,idolRepository,idolTodayService,idolVideoInfoService,initRankingService,youtubeAPIUtility);
        this.idolRepository = idolRepository;
        this.idolTodayService = idolTodayService;
        this.youtubeAPIUtility = youtubeAPIUtility;
    }

    @Override
    @Transactional
    public void updateIdolCountProperties()
    {
        List<Idol> updateIdolList;
        //300명 가져옴
        updateIdolList = idolRepository.findTop300ByCountryOrderBySubscriberCountDesc("JP");

        idolTodayService.initIdolToday();
        List<String> channelIdList = new ArrayList<>();
        updateIdolList.forEach((idol) -> channelIdList.add(idol.getChannelId()));

        //최대 300명의 리스트 가져온다
        List<String> channelIdsQueryList = youtubeAPIUtility.listSplitByUnitNum(channelIdList,50);

        updateIdolFromChannelIdsQuery(channelIdsQueryList);
    }

}


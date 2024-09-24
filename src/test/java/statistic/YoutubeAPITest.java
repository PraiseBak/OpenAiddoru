package statistic;

import com.aiddoru.dev.DevApplication;
import com.aiddoru.dev.Service.Rank.IdolVideoInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DevApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(TestRankService.class)
public class YoutubeAPITest {

    @Autowired
    private TestRankService testRankService; // RankService 대신 TestRankService 주입

    @Autowired
    private IdolVideoInfoService videoInfoService;

    /*
    소모량 몇? 매우 적게 소모함
    이부분 100미만이라고 잡아도될듯
     */
    @Test
    public void 유튜브_API_테스트() {
        testRankService.updateIdolCountProperties();
    }


    /**
     * 580명에 대해서 1200정도 소비됨
     */
    @Test
    public void 검색기반_유튜브_갱신_테스트(){
        testRankService.updateRankBySearch();
    }

    /**
     * 채널 하나에 대해서 100 할당량 소모
     */
    @Test
    public void 최근_영상_갱신_테스트(){
        List<String> channelIdList = new ArrayList<>();
        channelIdList.add("UC_j-1LnhHJeoQ2c_mCphw0w");

        videoInfoService.updateRecentVideo(channelIdList);
    }
}
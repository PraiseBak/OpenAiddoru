package com.aiddoru.dev.Controller.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Service.Rank.*;
import com.aiddoru.dev.Utility.YoutubeAPI.YoutubeAPIUtility;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/api/idol")
@AllArgsConstructor
public class RankAPIController {
    private IdolService idolService;
    private RankService rankService;
    private InitRankingService initRankingService;
    private IdolTodayService idolTodayService;
    private YoutubeAPIUtility youtubeAPIUtility;
    private IdolVideoInfoService idolVideoInfoService;


    //test json타입으로 반환하기 위함으로 리턴 타입을 responseEntity사용
    @GetMapping("/rank")
    public List<Idol> getRankingMap(
            @RequestParam(required = false,defaultValue = "1") long page,
            @RequestParam(required = false,defaultValue = "") String country
            ) {
        try {
            List<Idol> idolList;
            idolList = idolService.getVTuberListOrderByRanking(page,country);
            return idolList;
        }
        catch(Exception e) {
            //만약 에러가 떳을경우 메세지 저장
            String error = e.getMessage();
            // 반환값에 data는 널이고 에러반환값 저장
            //굳이 이렇게 해야하는 이유?
//            ResponseDTO<String> response = ResponseDTO.<String>builder().error(error).build();
            // 배드리퀘스트로 에러메세지 보냄
            System.out.println(error);
            return null;
//            return ResponseEntity.badRequest().body(error);
        }
    }


    /*
    검색
     */
    @PostMapping("/rankSearch")
    public List<Idol> searchRank()
    {
        try
        {
            rankService.updateRankBySearch();
            return idolService.getVTuberListOrderByRanking();
        } catch (Exception e) {
        }
        return null;
    }

    //초기 통계설정
    @GetMapping("/setInitRanking")
    public List<Idol> setInitRanking(){
        initRankingService.setInitRanking();
        return idolService.getVTuberListOrderByRanking();
    }

    @PostMapping("/updateClick")
    public void clickUpdate(@RequestParam Long idolId){
        Idol idol = idolService.findById(idolId);
        idolTodayService.clickTodayIdol(idol);
    }

    /*
    최근 상승세 리턴
     */
    @GetMapping("/recentHotIdol")
    public List<Idol> recentHotIdol(@RequestParam(required = false,defaultValue = "") String country)
    {
        return rankService.getRecentHotIdol(country);
    }

    /*
    전체국가중 최근 상승세순 리턴
     */
    @GetMapping("/noCountry")
    public ResponseEntity<?> recentHotIdol(
                                    @RequestParam(required = false,defaultValue = "1") long page
    )
    {
        List<Idol> idolList = idolService.getVTuberListOrderByRankingAndNoCountry(page);
        return ResponseEntity.ok().body(idolList);
    }

    @GetMapping("/recentClickIdol")
    public ResponseEntity<List<Idol>> mostClickedIdols(){
        return ResponseEntity.ok(rankService.getMostClickedIdol());
    }

}

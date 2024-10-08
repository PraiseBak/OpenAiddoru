package com.aiddoru.dev.Utility.Rank;

import com.aiddoru.dev.DTO.CachedIdolDto;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Rank.IdolSubscriberTrace;
import com.aiddoru.dev.Domain.Entity.Video.IdolVideoInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class RankUtility {

    public static List<Idol> getDuplicateRemoveIdolList(List<Idol> listA,List<Idol> listB){
        HashSet<Idol> set = new HashSet<>();
        set.addAll(listA);
        set.addAll(listB);
        return set.stream().toList();
    }

    /**
     * 각 idolList중 필요 이상으로 넘치는 리스트를 제한합니다
     * 제한되는 클래스 : IdolVideInfo
     * IdolSubscriberTrace
     * @return
     */
    public static List<Idol> returnIdolWithoutToMuchInformation(List<Idol> idolList) {
        //리스트를 제한된 개수만큼만 리턴하도록
        for(Idol idol : idolList){
            int idolTraceStatisticMaxLen =  Math.min(idol.getIdolSubscriberTraceList().size(), 30);
            List<IdolSubscriberTrace> idolSubscriberTraceList = new ArrayList<>(idol.getIdolSubscriberTraceList().subList(0, idolTraceStatisticMaxLen));
            Collections.reverse(idolSubscriberTraceList);
            idol.setIdolSubscriberTraceList(idolSubscriberTraceList);

            int idolVideoMaxNum = Math.min(idol.getIdolVideoInfoList().size(),10);
            List<IdolVideoInfo> idolVideoInfoList = new ArrayList<>(idol.getIdolVideoInfoList().subList(0, idolVideoMaxNum));
            idol.setIdolVideoInfoList(idolVideoInfoList);
        }
        return idolList;
    }


    public static List<CachedIdolDto> returnIdolDtoWithoutToMuchInformation(List<CachedIdolDto> idolList) {
//        for(CachedIdolDto idol : idolList){
//            int idolTraceStatisticMaxLen =  Math.min(idol.getIdolSubscriberTraceList().size(), 30);
//            List<IdolSubscriberTrace> idolSubscriberTraceList = new ArrayList<>(idol.getIdolSubscriberTraceList().subList(0, idolTraceStatisticMaxLen));
//            Collections.reverse(idolSubscriberTraceList);
//            idol.setIdolSubscriberTraceList(idolSubscriberTraceList);
//
//            int idolVideoMaxNum = Math.min(idol.getIdolVideoInfoList().size(),10);
//            List<IdolVideoInfo> idolVideoInfoList = new ArrayList<>(idol.getIdolVideoInfoList().subList(0, idolVideoMaxNum));
//            idol.setIdolVideoInfoList(idolVideoInfoList);
//        }
        return idolList;

    }
}

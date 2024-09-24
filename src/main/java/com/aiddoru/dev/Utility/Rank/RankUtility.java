package com.aiddoru.dev.Utility.Rank;

import com.aiddoru.dev.Domain.Entity.Rank.Idol;

import java.util.HashSet;
import java.util.List;

public class RankUtility {

    public static List<Idol> getDuplicateRemoveIdolList(List<Idol> listA,List<Idol> listB){
        HashSet<Idol> set = new HashSet<>();
        set.addAll(listA);
        set.addAll(listB);
        return set.stream().toList();
    }

}

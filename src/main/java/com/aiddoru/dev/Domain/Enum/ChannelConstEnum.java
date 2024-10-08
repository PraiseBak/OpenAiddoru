package com.aiddoru.dev.Domain.Enum;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum ChannelConstEnum {
    VTuber(0, new String[]{"vtuber"});

    private int idx;
    private List<String> searchKeyword;

    ChannelConstEnum(int idx, String[] keywords) {
        this.idx = idx;
        this.searchKeyword = Arrays.asList(keywords);
    }
}
package com.aiddoru.dev.Utility;

import com.aiddoru.dev.Domain.Entity.Community.Community;
import com.aiddoru.dev.Service.Community.CommunityService;
import com.aiddoru.dev.Service.Rank.IdolVideoInfoService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

public class RedisHelper {
    public final static String tagWeightSetUserIdPostfix = "zset:";
    public final static String idolUserSetIdPostfix = "set:";
    //idolRankSet:2019-03-06_KR_01
    public final static String idolRankSetIdKeyPattern = "idolRankSet:%s_%s_%s";

}

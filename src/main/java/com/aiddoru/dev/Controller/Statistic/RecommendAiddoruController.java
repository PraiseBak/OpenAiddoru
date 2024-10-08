package com.aiddoru.dev.Controller.Statistic;

import com.aiddoru.dev.DTO.Auth.UserRecommendRatingsRequestDto;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Domain.Entity.Recommend.Tag;
import com.aiddoru.dev.Service.Rank.IdolService;
import com.aiddoru.dev.Service.Rank.RecommendAiddoruService;
import com.aiddoru.dev.Service.Rank.TagService;
import com.aiddoru.dev.Service.User.AuthService;
import com.aiddoru.dev.Utility.Rank.RankUtility;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RequestMapping(value = "/api/recommendAiddoru")
@RestController
@RequiredArgsConstructor
public class RecommendAiddoruController {

    private final RecommendAiddoruService recommendAiddoruService;

    private final IdolService idolService;
    private final TagService tagService;
    private final AuthService authService;

    @PostMapping("/")
    public ResponseEntity<?> recommendAiddoru(@RequestBody UserRecommendRatingsRequestDto userRecommendRatingsRequestDto) {
        String recommendedAiddoruName = recommendAiddoruService.recommendCreator(userRecommendRatingsRequestDto.getRatings());
        Idol idol = idolService.findByIdolName(recommendedAiddoruName);
        return ResponseEntity.ok(idol);
    }


    /**
     * @param authentication
     * @param annonymousUserId
     * @param response
     * @return recommendedIdolList
     */

    //TODO 성능 개선
    @GetMapping("/")
    public ResponseEntity<List<Idol>> getRecommendAiddoru(Authentication authentication,
                                                 @CookieValue(value = AuthService.USER_COOKIE_NAME, defaultValue = "") String annonymousUserId,
                                                 HttpServletResponse response) {
        String userId = authService.getUserHistoryId(authentication,response,annonymousUserId);
        Set<ZSetOperations.TypedTuple<Object>> tagSet = tagService.getTagListByUserHistory(userId);
        List<Idol> resultIdolList = tagService.recommendIdolByTag(userId,tagSet);
        resultIdolList = RankUtility.returnIdolWithoutToMuchInformation(resultIdolList);
        return ResponseEntity.ok(resultIdolList);
    }

}

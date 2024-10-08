package com.aiddoru.dev.Cache.Rank;

import com.aiddoru.dev.Config.RedisConfig;
import com.aiddoru.dev.DTO.CachedIdolDto;
import com.aiddoru.dev.Domain.Entity.Rank.Idol;
import com.aiddoru.dev.Service.Rank.IdolService;
import com.aiddoru.dev.Utility.RedisHelper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class IdolRankCache {
    @Qualifier("idolRedisTemplate")
    private final RedisTemplate<String, RedisConfig.CachedIdolDtos> redisTemplate;
    private final IdolService idolService;

    private String createCacheKey(String country, long page){
        String date = LocalDateTime.now().toLocalDate().toString();
        String key = String.format(RedisHelper.idolRankSetIdKeyPattern,date,country,page+"");
        return key;
    }

    public boolean isExistsKey(String key){
        if(redisTemplate.opsForValue().get(key) == null) return false;
        return true;
    }


    @Transactional
    public List<CachedIdolDto> getIdolRankCache(String country, long page){
        String key = createCacheKey(country,page);

        if(isExistsKey(key)){
            RedisConfig.CachedIdolDtos cachedIdolDtos = redisTemplate.opsForValue().get(key);
            return cachedIdolDtos.retCachedIdolDtoList();
        }

        List<Idol> savedIdolList = idolService.getVTuberListOrderByRanking(page,country);

        return saveCacheIdolList(key,savedIdolList);
    }

    private List<CachedIdolDto> saveCacheIdolList(String key, List<Idol> idolList) {
        List<CachedIdolDto> cachedIdolDtos = idolList.stream()
                .map(Idol::toCachedIdolDto)
                .toList();
        redisTemplate.opsForValue().set(key, new RedisConfig.CachedIdolDtos(cachedIdolDtos));
        return cachedIdolDtos;
    }



}

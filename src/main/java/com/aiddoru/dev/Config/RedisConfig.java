package com.aiddoru.dev.Config;


import com.aiddoru.dev.DTO.CachedIdolDto;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RedisConfig {

    @Bean("defaultRedisTemplate")
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }


    @Setter
    @Getter
    public static class CachedIdolDtos {
        private List<CachedIdolDto> cachedIdolDtoList;

        // @JsonCreator와 @JsonProperty를 사용하여 생성자 정의
        @JsonCreator
        public CachedIdolDtos(@JsonProperty("cachedIdolDtoList") List<CachedIdolDto> cachedIdolDtoList) {
            this.cachedIdolDtoList = cachedIdolDtoList;
        }

        public CachedIdolDtos() {
            this.cachedIdolDtoList = new ArrayList<>();
        }

        public List<CachedIdolDto> retCachedIdolDtoList() {
            return cachedIdolDtoList;
        }
    }


    @Bean("idolRedisTemplate")
    public RedisTemplate<String, CachedIdolDtos> idolRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, CachedIdolDtos> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        ObjectMapper objectMapper =new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);
        return template;
    }
    /**
     *
     *
     * 넵Could not read JSON:Unexpected token (START_OBJECT), expected VALUE_STRING: need String, Number of Boolean value that contains type id (for subtype of java.lang.Object)
     *  at [Source: (byte[])"[{"@class":"com.aiddoru.dev.DTO.CachedIdolDto","id":2,"channelName":"test1","channelId":"test1","subscriberCount":["java.math.BigInteger",63714673],"viewCount":["java.math.BigInteger",1],"videoCount":["java.math.BigInteger",1],"channelUrl":"test1","channelThumbnailURL":null,"country":"kr","publishedAt":["java.sql.Timestamp",1721806965956],"description":null,"idolToday":{"@class":"com.aiddoru.dev.Domain.Entity.Rank.IdolToday","id":519,"subscribeDiff":["java.math.BigInteger",0],"subscribeDiffPerce"[truncated 6210 bytes]; line: 1, column: 2]
     */

//    @Bean
//    public RedisTemplate<String, List<CachedIdolDto>> idolRedisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, List<CachedIdolDto>> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setKeySerializer(new StringRedisSerializer());
//        //test용
////        template.delete(template.keys("*")); // 모든 키 삭제
//
//        // ObjectMapper 생성 및 설정
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new JavaTimeModule()); // Java 8 시간 모듈 등록
//        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false); // 타임스탬프 형식으로 쓰지 않음
//        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//
//        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
//        serializer.setObjectMapper(objectMapper);
//
//        template.setValueSerializer(serializer);
//        template.setHashValueSerializer(serializer);
//        return template;
//    }

}

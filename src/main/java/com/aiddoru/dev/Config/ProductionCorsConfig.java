package com.aiddoru.dev.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Profile("production")
@Configuration
public class ProductionCorsConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
//                .allowedOrigins("https://aiddoru.co.kr:80")
                .allowedOrigins("http://localhost:3000")
//                .allowedOrigins("https://aiddoru.co.kr:80")
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 허용할 HTTP 메서드
                .allowedHeaders("*") // 허용할 HTTP 헤더
                .allowCredentials(true); // 크로스 도메인 쿠키 허용
    }

}

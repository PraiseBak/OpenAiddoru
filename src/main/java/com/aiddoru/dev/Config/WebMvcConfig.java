package com.aiddoru.dev.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${upload.path}") // properties 파일에서 이미지 저장 경로를 지정하세요
    private String uploadPath;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry
                .addResourceHandler("/img/")
                .addResourceLocations("file:///" + uploadPath);
    }

}
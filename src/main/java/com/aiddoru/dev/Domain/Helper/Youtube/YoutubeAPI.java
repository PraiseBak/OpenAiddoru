package com.aiddoru.dev.Domain.Helper.Youtube;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;


@Component
@Slf4j
public class YoutubeAPI {


    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    private YouTube service;

    public YoutubeAPI() {
        try {
            final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            service =  new YouTube.Builder(httpTransport, JSON_FACTORY, getRequestInitializer())
                    .setApplicationName("aiddoru")
                    .build();
        } catch (GeneralSecurityException | IOException e) {
            log.error("YoutubeAPI 초기화에 실패하였습니다.");
            service = null; // 서비스를 초기화하지 못했을 때 service를 null로 설정
        }
    }

    public YouTube getService() throws IOException, GeneralSecurityException {
        return service;
    }

    private static HttpRequestInitializer getRequestInitializer() {
        return request -> {
            request.setConnectTimeout(5 * 60000);  // 5 minutes
            request.setReadTimeout(5 * 60000);  // 5 minutes
        };
    }

}



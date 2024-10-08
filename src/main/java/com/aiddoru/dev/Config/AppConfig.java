package com.aiddoru.dev.Config;


import com.aiddoru.dev.AOP.TimeLogAOP;
import com.praiseutil.timelog.utility.LogTrace;
import com.praiseutil.timelog.utility.TraceLocalLogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public LogTrace logTrace() {
        return new TraceLocalLogTrace();
    }
}
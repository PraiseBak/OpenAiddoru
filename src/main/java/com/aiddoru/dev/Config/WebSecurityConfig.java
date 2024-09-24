package com.aiddoru.dev.Config;

import com.aiddoru.dev.Utility.Security.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.util.pattern.PathPatternParser;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtFilter jwtFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PathPatternParser pathPatternParser() {
        return new PathPatternParser();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        RequestMatcher nonGetRequests = (request) -> !HttpMethod.GET.matches(request.getMethod());

        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((req) -> req

                        .requestMatchers("/api/img/add")
                            .hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/api/img/**")
                            .permitAll()
                        .requestMatchers("/api/ROLE_ADMIN/community/**")
                            .hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/community/**")
                            .permitAll()
                        .requestMatchers("/api/thread/{communityName}/thread")
                            .hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/api/thread/{communityName}/{threadId}")
                            .permitAll()
                        .requestMatchers("/api/auth/**")
                            .permitAll()
                        .requestMatchers("/api/comment/{communityName}/{threadId}")
                            .hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/api/comment/{communityName}/{threadId}/{commentId}")
                            .hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/api/heart/{communityName}/{commentId}/comment")
                            .hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/api/heart/{communityName}/{threadId}")
                            .hasAnyAuthority("ROLE_USER","ROLE_ADMIN")
                        .requestMatchers("/api/statistic/**")
                                .permitAll()
                        .requestMatchers("/api/idol/rankSearch")
                            .hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/idol/setInitRanking")
                            .hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("/api/idol/rankSearch")
                            .hasAnyAuthority("ROLE_ADMIN")

                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}

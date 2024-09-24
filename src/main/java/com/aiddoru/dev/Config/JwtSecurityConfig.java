package com.aiddoru.dev.Config;

import com.aiddoru.dev.Service.User.UserService;
import com.aiddoru.dev.Utility.Security.JwtFilter;
import com.aiddoru.dev.Utility.Security.SecurityUtil;
import com.aiddoru.dev.Utility.Security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtFilter customFilter;


    @Override
    public void configure(HttpSecurity http) {
//        JwtFilter customFilter = new JwtFilter(tokenProvider,new SecurityUtil(tokenProvider),userService);
        http.addFilterBefore(customFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
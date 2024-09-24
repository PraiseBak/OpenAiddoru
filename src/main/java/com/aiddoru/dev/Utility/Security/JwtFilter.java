package com.aiddoru.dev.Utility.Security;

import com.aiddoru.dev.Service.User.AuthService;
import com.aiddoru.dev.Service.User.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final SecurityUtil securityUtil;
    //usreService를 통해 가져오는것은 괜찮은지 한번 생각해봐야함
    private final UserService userService;

    //허가 url 이외 모든 요청은 이 필터 거치고 토큰이 없거나 유효하지 않으면 fail처리한다
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = securityUtil.resolveToken(request);

        try{
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                String username = authentication.getName();
                if(userService.isBlockedUser(username)){
                    System.out.println("blocked user" + username);
                    return;
                }
            }
        }catch (ExpiredJwtException e){
            log.info("만료된 토큰입니다" + jwt);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }


}

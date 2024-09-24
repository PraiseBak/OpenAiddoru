package com.aiddoru.dev.Utility.Security;

import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.SecureRandom;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final TokenProvider tokenProvider;
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String REFRESH_COOKIE_HEADER = "refreshToken";

    public static Long getCurrentUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw new RuntimeException("Security Context에 인증 정보가 없습니다.");
        }
        return Long.parseLong(authentication.getName());
    }

    public String getUsernameFromRequest(HttpServletRequest request){
        String token = resolveToken(request);
//        String token = getTokenFromCookie(request);
        return tokenProvider.getUsernameFromToken(token);
    }

    //access token은 cookie에 저장하지 않음
    //deprecated
    private String getTokenFromCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    // 쿠키값을 확인하고 필요한 작업 수행
                    return token;
                }
            }
        }
        return "";
    }



    public String getPrincipleFromToken(HttpServletRequest request){
        String token = resolveToken(request);
        return tokenProvider.getPrincipleFromToken(token);
    }

    public String getPrincipleFromToken(String token){
        return tokenProvider.getPrincipleFromToken(token);
    }


    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return bearerToken;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (REFRESH_COOKIE_HEADER.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    // 쿠키값을 확인하고 필요한 작업 수행
                    return token;
                }
            }
        }
        return null;
    }

    public String getNewAccessCode(HttpServletRequest request)
    {
        String refreshToken = resolveRefreshToken(request);
        //통과시에 그냥 하나 만들어줘야함
        if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
            Authentication authentication = tokenProvider.getAuthentication(refreshToken);
            return tokenProvider.generateAccessToken(authentication);
        }
        throw new CustomException(CustomErrorCode.INVALID_AUTH_TOKEN);
    }

    public void deleteRefreshCookie(HttpServletResponse response)
    {
        Cookie deletedRefreshTokenCookie = new Cookie("refreshToken", null);
        deletedRefreshTokenCookie.setMaxAge(0);
        deletedRefreshTokenCookie.setPath("/"); // 쿠키의 경로를 설정합니다. 필요에 따라 경로를 조정하세요.
        response.addCookie(deletedRefreshTokenCookie);
    }

    public static String generateRandomPassword() {
        // 랜덤 비밀번호의 길이 범위 설정 (8에서 20 사이)
        int minLength = 8;
        int maxLength = 20;

        // 비밀번호에 사용할 문자열
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_+=<>?";

        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // 비밀번호 길이를 랜덤하게 선택
        int passwordLength = minLength + random.nextInt(maxLength - minLength + 1);

        for (int i = 0; i < passwordLength; i++) {
            // 랜덤한 문자 선택
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);

            // 비밀번호에 추가
            password.append(randomChar);
        }

        return password.toString();
    }

    public String getUsernameFromToken(String token) {
        return tokenProvider.getUsernameFromToken(token);
    }
}

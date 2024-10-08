package com.aiddoru.dev.DTO.Auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class LoginResponse {
    private String role;
    private String accessToken;
    private String profileImgSrc;
    public LoginResponse(String role, String accessToken,String profileImgSrc) {
        this.role = role;
        this.accessToken = accessToken;
        this.profileImgSrc = profileImgSrc;
    }

}

package com.aiddoru.dev.DTO.Auth;

import com.aiddoru.dev.Domain.Entity.User.User;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {
    private String email;
    private String username;
    private String profileImgSrc;


    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .profileImgSrc(user.getProfileImgSrc())
                .build();
    }
}

package com.aiddoru.dev.Domain.Entity.User;

import com.aiddoru.dev.Domain.Enum.Authority;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class SignupRequestUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 4,max = 40)
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8,max = 20)
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @Builder.Default
    private String profileImgSrc = "";

    @NotNull
    private String confirmCode = "";


    private LocalDateTime untilRequestDate;

    private boolean isAuthenticated;

    public User toUser(){


        return User.builder()
                .password(password)
                .username(username)
                .email(email)
                .authority(Authority.ROLE_USER)
                .profileImgSrc(profileImgSrc)
                .build();
    }


    public void setConfirmCode(String confirmCode) {
        this.confirmCode = confirmCode;
    }

    public void setUntilRequestDate(LocalDateTime untilRequestDate) {
        this.untilRequestDate = untilRequestDate;
    }
}

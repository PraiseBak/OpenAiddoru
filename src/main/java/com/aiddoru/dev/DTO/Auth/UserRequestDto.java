package com.aiddoru.dev.DTO.Auth;

import com.aiddoru.dev.Domain.Entity.User.SignupRequestUser;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Authority;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequestDto {

    @NotBlank
    @NotNull
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @Size(min = 4, max = 40)
    private String email;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "유저네임은 영문자 및 숫자로만 이루어져야 합니다.")
    @Size(min = 8, max = 20)
    private String username;

    @NotBlank
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8자 이상이어야 합니다.")
    private String password;

    private String profileImgSrc = "";

    public User toUser(PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .authority(Authority.ROLE_USER)
                .build();
    }



    public SignupRequestUser toSignupRequestUser(PasswordEncoder passwordEncoder, String confirmCode){
        return SignupRequestUser.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .username(username)
                .profileImgSrc(profileImgSrc)
                .confirmCode(confirmCode)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(username, password);
    }

    // 예시: 비밀번호 패턴 검사

}

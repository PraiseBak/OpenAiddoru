package com.aiddoru.dev.DTO.Auth;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UserChangeInfoRequestDto {

    @NotBlank
    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "유저네임은 영문자 및 숫자로만 이루어져야 합니다.")
    String username;

    String password;

    @NotBlank
    @NotNull
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @Size(min = 4, max = 40)
    String email;


    public boolean isPasswordValid() {
        if (password == null) {
            return false; // 비밀번호가 null이면 유효하지 않음
        }

        // 비밀번호 패턴 검사
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z@$!%*?&]{8,}$";
        return password.matches(pattern);
    }
}


package com.aiddoru.dev.Domain.Entity.User;

import com.aiddoru.dev.DTO.Auth.UserChangeInfoRequestDto;
import com.aiddoru.dev.Domain.Entity.Community.Comment;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Domain.Enum.Authority;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 4,max = 40)
    @Email(message = "올바른 이메일 형식이어야 합니다.")
    private String email;

    @NotBlank
    @NotNull
    @Size(min = 8,max = 20)
    @Column(name="username" , unique=true)
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "유저네임은 영문자 및 숫자로만 이루어져야 합니다.")
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 8)
    private String password;

    @NotNull
    @Builder.Default
    private String profileImgSrc = "";

    @Enumerated(EnumType.STRING)
    private Authority authority;

    private LocalDateTime untilBlockDate;

    private boolean isBlocked;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Test> testWriteList = new ArrayList<>();

    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Thread>  writeThreadList = new ArrayList<>();

    @OneToMany(mappedBy = "user",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private final List<Comment> commentList = new ArrayList<>();


    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    public void addComment(Comment comment) {
        comment.setUser(this);
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    public void setBlockUntilDate(LocalDateTime untilBlockDate) {
        this.untilBlockDate = untilBlockDate;
    }

    public void setPassword(String newPassword) {
        this.password = newPassword;
    }

    public void changeUserInfo(UserChangeInfoRequestDto userChangeInfoRequestDto, String changedProfileImgSrc, PasswordEncoder passwordEncoder) {
        this.username = userChangeInfoRequestDto.getUsername();
        String changePassword = userChangeInfoRequestDto.getPassword();
        if(changePassword.length() >= 8){
            if(isPasswordValid(changePassword)){
                this.password = passwordEncoder.encode(userChangeInfoRequestDto.getPassword());
            }else{
                throw new CustomException(CustomErrorCode.INVALID_RESOURCE);
            }
        }

        this.email = userChangeInfoRequestDto.getEmail();

        if(changedProfileImgSrc != null){
            this.profileImgSrc = changedProfileImgSrc;
        }
    }

    public boolean isPasswordValid(String password) {
        String pattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,40}$";
        return password.matches(pattern);
    }

}

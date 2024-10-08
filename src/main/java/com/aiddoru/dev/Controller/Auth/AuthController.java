package com.aiddoru.dev.Controller.Auth;

import com.aiddoru.dev.DTO.Auth.*;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Service.User.AuthService;
import com.aiddoru.dev.Service.User.EmailService;
import com.aiddoru.dev.Utility.Security.SecurityUtil;
import com.aiddoru.dev.Utility.Security.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final SecurityUtil securityUtil;
    private final EmailService emailService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/signupConfirm")
//    public ResponseEntity<Void> signup(@RequestParam(required = true) String username,@RequestParam(required = true) String confirmCode) {
    public ResponseEntity<Void> signup(@RequestBody ConfirmSignupRequest confirmSignupRequest) {
        authService.signup(confirmSignupRequest.getUsername(),confirmSignupRequest.getConfirmCode());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/findPw")
    public ResponseEntity<Void> findId(@RequestBody FindUserInfoDto findUserInfoDto) {
        authService.findPW(findUserInfoDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/findId")
    public ResponseEntity<Void> signup(@RequestBody FindUserInfoDto findUserInfoDto) {
        authService.findId(findUserInfoDto);
        return ResponseEntity.ok().build();
    }


    //이메일로 인증요청보냄
    @PostMapping("/requestSignup")
    public ResponseEntity<?> requestSignup(@RequestBody @Validated UserRequestDto requestDto){
        log.info("requestSignup 요청 들어옴" + requestDto.toString());
        authService.requestSignup(requestDto);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequestDto requestDto,HttpServletResponse response)  throws IOException {
        TokenDto tokenDto = authService.login(requestDto);
        //login 요청
        Cookie cookie = new Cookie("refreshToken",tokenDto.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        String role = securityUtil.getPrincipleFromToken(tokenDto.getAccessToken());
        String username = securityUtil.getUsernameFromToken(tokenDto.getAccessToken());
        String profileImgSrc = authService.getUserProfileImgSrc(username);
        LoginResponse loginResponse = new LoginResponse(role, tokenDto.getAccessToken(),profileImgSrc);
        return new ResponseEntity<>(loginResponse, HttpStatus.OK);
    }


    @GetMapping("/checkAdmin")
    public ResponseEntity<?> checkAdmin(HttpServletRequest request) {
        String principle = securityUtil.getPrincipleFromToken(request);
        return ResponseEntity.ok(principle.contains("ROLE_ADMIN"));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(HttpServletRequest request){
        String newAccessToken = securityUtil.getNewAccessCode(request);
        return ResponseEntity.ok(newAccessToken);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response){
        securityUtil.deleteRefreshCookie(response);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/checkUser")
    public ResponseEntity<UserResponseDto> modifyUser(@RequestBody UserRequestDto userRequestDto, Authentication authentication){
        if(!Objects.equals(authentication.getName(), userRequestDto.getUsername())){
            throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
        }

        if(authService.login(userRequestDto) != null){
            User user = authService.getUserByUsername(userRequestDto.getUsername());
            UserResponseDto userResponseDto = UserResponseDto.builder()
                    .profileImgSrc(user.getProfileImgSrc())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
        }

        throw new CustomException(CustomErrorCode.USER_NOT_FOUND);
    }


    @PostMapping("/modifyUserInfo")
    public ResponseEntity<String> modifyUser(
            @RequestPart(value = "profileImg",required = false) MultipartFile profileImg,
            @RequestPart(value = "userChangeInfoRequestDto",required = false) @Validated UserChangeInfoRequestDto userChangeInfoRequestDto,
            Authentication authentication
    ) {
         authService.changeUserInfo(userChangeInfoRequestDto, authentication.getName(), profileImg);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/getUserResponseDto")
    public ResponseEntity<UserResponseDto> getUserByUsername(@RequestParam String username){
        User user = authService.getUserByUsername(username);
        return ResponseEntity.ok().body(UserResponseDto.of(user));
    }

    @GetMapping("/checkDuplicateUserInfo")
    public ResponseEntity<Void> checkDuplicateUserInfo(@ModelAttribute UserChangeInfoRequestDto userChangeInfoRequestDto,Authentication authentication) {
        authService.checkDuplicateUserInfo(userChangeInfoRequestDto,authentication == null ? null : authentication.getName());
        return ResponseEntity.ok().build();
    }

}

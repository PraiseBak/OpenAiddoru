package com.aiddoru.dev.Service.User;

import com.aiddoru.dev.DTO.Auth.*;
import com.aiddoru.dev.Domain.Entity.User.SignupRequestUser;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Persistence.User.SignupRequestUserRepository;
import com.aiddoru.dev.Persistence.User.UserRepository;
import com.aiddoru.dev.Service.Community.ImageService;
import com.aiddoru.dev.Utility.Security.SecurityUtil;
import com.aiddoru.dev.Utility.Security.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder managerBuilder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final SignupRequestUserRepository signupRequestUserRepository;
    private final EmailService emailService;
    private final ImageService imageService;
    public static final String USER_COOKIE_NAME = "aiddoruAnonymousUserId";



    public TokenDto login(UserRequestDto requestDto) {
        UsernamePasswordAuthenticationToken authenticationToken = requestDto.toAuthentication();
        Authentication authentication = managerBuilder.getObject().authenticate(authenticationToken);
        return tokenProvider.generateTokenDto(authentication);
    }


    @Transactional
    public UserResponseDto changeUserPassword(String email, String exPassword, String newPassword) {
        User user = userRepository.findById(SecurityUtil.getCurrentUserId()).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        if (!passwordEncoder.matches(exPassword, user.getPassword())) {
            throw new RuntimeException("비밀번호가 맞지 않습니다");
        }
        user.changePassword(passwordEncoder.encode((newPassword)));
        return UserResponseDto.of(userRepository.save(user));
    }


    /*
    이메일로 회원가입 요청
     */
    @Transactional
    public void requestSignup(UserRequestDto requestDto) {
        /*
        이메일, 유저네임 중복되면 안됨
         */
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new CustomException(CustomErrorCode.DUPLICATE_USER);
        }

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(CustomErrorCode.DUPLICATE_EMAIL);
        }

        String username = requestDto.getUsername();
        String tmpCode = new Random().toString() + username;
        String confirmCode =  passwordEncoder.encode(tmpCode);
        Optional<SignupRequestUser> signupRequestUserOptional = signupRequestUserRepository.findByUsernameAndEmail(requestDto.getUsername(),requestDto.getEmail());

        //이미 존재하면 갱신만해야함
        if(signupRequestUserOptional.isPresent()){
            SignupRequestUser signupRequestUser = signupRequestUserOptional.get();
            signupRequestUser.setConfirmCode(confirmCode);
            LocalDateTime now = LocalDateTime.now();
            signupRequestUser.setUntilRequestDate(now.plusHours(6L));
        }
        //request user가 존재하지 않는경우 -> 새로 생성
        else{
            SignupRequestUser signupRequestUser = requestDto.toSignupRequestUser(passwordEncoder,confirmCode);
            LocalDateTime now = LocalDateTime.now();
            signupRequestUser.setUntilRequestDate(now.plusHours(6L));
            signupRequestUserRepository.save(signupRequestUser);
        }

        emailService.sendSignupEmail(requestDto.getEmail(),username,confirmCode);
    }


    @Transactional
    public void signup(String username, String confirmCode) {
        System.out.println(username +"," + confirmCode);
        Optional<SignupRequestUser> signupRequestUserOptional = signupRequestUserRepository.findByUsernameAndConfirmCode(username, confirmCode);

        signupRequestUserOptional.orElseThrow(() -> new CustomException(CustomErrorCode.INVALID_AUTH_TOKEN));
        SignupRequestUser signupRequestUser = signupRequestUserOptional.get();

        if (signupRequestUser.getUntilRequestDate().isBefore(LocalDateTime.now())) {
            throw new CustomException(CustomErrorCode.REQUEST_TIME_PASSED);
        }

        User user = signupRequestUser.toUser();

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new CustomException(CustomErrorCode.DUPLICATE_USER);
        }

        userRepository.save(user);
    }

    @Transactional
    public void findPW(FindUserInfoDto findUserInfoDto) {
        String email = findUserInfoDto.getEmail();
        String username = findUserInfoDto.getUsername();
        Optional<User> user = userRepository.findByEmailAndUsername(email,findUserInfoDto.getUsername());
        if(user.isPresent()){
            String newPassword = SecurityUtil.generateRandomPassword();
            user.get().setPassword(passwordEncoder.encode(newPassword));
            emailService.sendFindPwEmail(
                    findUserInfoDto.getEmail(),findUserInfoDto.getUsername(),newPassword);
        }

        //email user 동일한지
    }

    public void findId(FindUserInfoDto findUserInfoDto) {
        String email = findUserInfoDto.getEmail();
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()){
            String username = user.get().getUsername();
            emailService.sendFindIdEmail(email,username);
        }

    }

    @Transactional
    public void changeUserInfo(UserChangeInfoRequestDto userChangeInfoRequestDto, String requestUsername, MultipartFile profileImg) {
        User user = userRepository.findByUsername(requestUsername).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
        String profileImgSrc = null;
        if(profileImg != null){
            try{
                profileImgSrc = imageService.saveImage(profileImg);
            }catch (IOException ignored){

            }
        }
        user.changeUserInfo(userChangeInfoRequestDto,profileImgSrc,passwordEncoder);
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    public void checkDuplicateUserInfo(UserChangeInfoRequestDto userChangeInfoRequestDto,String requestUsername) {
        String username = userChangeInfoRequestDto.getUsername();
        String email = userChangeInfoRequestDto.getEmail();

        Optional<User> requestUserOptional = userRepository.findByUsername(requestUsername);
        boolean duplicateEmail = false;
        boolean duplicateUsername = false;

        //chnageUserInfo인 경우
        if(requestUserOptional.isPresent()){
            User requestUser = requestUserOptional.get();

            if(!requestUser.getEmail().equals(email)){
                duplicateEmail = userRepository.existsByEmail(email);
            }
            if(!requestUser.getUsername().equals(username)){
                duplicateUsername = userRepository.existsByUsername(username);
            }
        }
        //changeUserInfo가아니라 회원가입요청인 경우
        else{
            duplicateEmail = userRepository.existsByEmail(email);
            duplicateUsername = userRepository.existsByUsername(username);
        }

        if(duplicateEmail && duplicateUsername){
            throw new CustomException(CustomErrorCode.DUPLICATE_USER_INFO);
        }

        if(duplicateEmail){
            throw new CustomException(CustomErrorCode.DUPLICATE_EMAIL);
        }

        if(duplicateUsername){
            throw new CustomException(CustomErrorCode.DUPLICATE_USER);
        }
    }

    public String getUserProfileImgSrc(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND))
                .getProfileImgSrc();

    }


    public String getUserHistoryId(Authentication authentication, HttpServletResponse response, String anonymousUserId){

        if(authentication == null || !authentication.isAuthenticated()) {
            if (anonymousUserId.isEmpty()) {
                anonymousUserId = UUID.randomUUID().toString();
                Cookie cookie = new Cookie(USER_COOKIE_NAME, anonymousUserId);
                cookie.setHttpOnly(true);
                cookie.setPath("/");
                cookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 기간 하루
                response.addCookie(cookie);
            }
            return anonymousUserId;
        }
        return authentication.getName();
    }
}

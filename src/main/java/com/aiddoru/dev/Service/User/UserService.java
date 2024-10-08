package com.aiddoru.dev.Service.User;


import com.aiddoru.dev.DTO.Auth.UserResponseDto;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Domain.Entity.User.Test;
import com.aiddoru.dev.Domain.Entity.User.User;
import com.aiddoru.dev.Domain.Enum.Authority;
import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import com.aiddoru.dev.Persistence.User.UserRepository;
import com.aiddoru.dev.Utility.Page.PageUtility;
import com.aiddoru.dev.Utility.Security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ClientInfoStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PageUtility pageUtil;



    public UserResponseDto getMyInfoBySecurity() {
        return userRepository.findById(SecurityUtil.getCurrentUserId())
                .map(UserResponseDto::of)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new CustomException(CustomErrorCode.USER_NOT_FOUND));
    }

    public List<User> getUserList(long page, String searchParam) {
        Pageable pageable = pageUtil.getPageable(page);
        if (searchParam.isEmpty()){
            return userRepository.findAll(pageable).stream().toList();
        }else{
            return userRepository.findByUsernameContains(searchParam,pageable);
        }
    }

    @Transactional
    public void block(long id, int blockDayVal) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다"));
        user.setAuthority(Authority.ROLE_BLOCK);

        LocalDateTime blockUntilDate;
        if(blockDayVal == -1){
            blockUntilDate = LocalDateTime.now().plusDays(Integer.MAX_VALUE);
        }else{
            blockUntilDate = LocalDateTime.now().plusDays(blockDayVal);
        }
        user.setBlockUntilDate(blockUntilDate);
    }

    public boolean isBlockedUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " 을 DB에서 찾을 수 없습니다"));
        //block이라면
        if(user.getAuthority() == Authority.ROLE_BLOCK){
            return true;
        }
        return false;
    }


    //block된 유저 다시 해제시켜줌
    @Scheduled(cron = "30 46 15 * * *")
    public void updateBlockUser(){
        List<User> blockedUser = userRepository.findAllByAuthority(Authority.ROLE_BLOCK);
        System.out.println("update blocked user status");
        for(User user : blockedUser){
            LocalDateTime untilDateTime = user.getUntilBlockDate();
            //차단일이 지났다면 초기화시켜준다

            if(untilDateTime != null && untilDateTime.isBefore(LocalDateTime.now())){
                user.setBlockUntilDate(null);
                user.setAuthority(Authority.ROLE_USER);
            }
            userRepository.save(user);
        }


    }

}

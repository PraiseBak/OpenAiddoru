package com.aiddoru.dev.Controller.User;

import com.aiddoru.dev.DTO.Auth.UserChangeInfoRequestDto;
import com.aiddoru.dev.DTO.Community.ThreadResponseDto;
import com.aiddoru.dev.Domain.Entity.Community.Thread;
import com.aiddoru.dev.Domain.Entity.User.Test;
import com.aiddoru.dev.Service.User.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.stylesheets.LinkStyle;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
@RequiredArgsConstructor
public class UserAPIController {

    private final UserService userService;

//    @GetMapping("/myWriteThread")
//    public ResponseEntity<List<ThreadResponseDto>> getUserWriteThread(
//            Authentication authentication
//    ) {
//        List<ThreadResponseDto> threadResponseDtoList = new ArrayList<>();
//        userService.getTest(authentication.getName());
////        for(Test thread : threadList){
////            System.out.println(thread);
////            threadResponseDtoList.add(ThreadResponseDto.fromThread(thread));
////        }
//        return ResponseEntity.ok(threadResponseDtoList);
//    }

}

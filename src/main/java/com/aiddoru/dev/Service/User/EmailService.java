package com.aiddoru.dev.Service.User;

import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import com.aiddoru.dev.Domain.Helper.Error.CustomException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final PasswordEncoder passwordEncoder;

    private final String FROM = "qkrrmsgma2@gmail.com";


    public String sendSignupEmail(String email, String username, String confirmCode) {
        String to = email;
        String subject = "aiddoru 인증 이메일입니다";

        // TODO: 2023-10-17 도메인에 맞춰 수정
        String content = "<h1>aiddoru 인증 이메일 " +
                "</h1><br><p>다음 링크를 눌러 이메일 인증을 완료해주세요</p>" +
                "<a href='https://aiddoru.co.kr/auth/signupConfirm?confirmCode=" + confirmCode + "&username=" + username + "'>Aiddoru 회원가입 이메일 인증하기!!!</a>";
        sendEmail(to,subject,content);
        return confirmCode;
    }

    public void sendEmail(String to, String subject, String content){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(FROM);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content,true);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new CustomException(CustomErrorCode.INVALID_RESOURCE);
        }
    }

    public void sendFindIdEmail(String email,String username) {
        String to = email;
        String subject = "aiddoru 아이디 찾기 이메일입니다";
        String content = "<h1>요청하신 아이디입니다</h1><br><p>" +  username + "</p>";
        sendEmail(to,subject,content);
    }

    public void sendFindPwEmail(String email, String username, String newPassword) {

        String to = email;
        String subject = "aiddoru 비밀번호 찾기 이메일입니다";
        String content = "<h1>임시 비밀번호입니다</h1><br><p>" +  newPassword + "</p>";
        sendEmail(to,subject,content);
    }



}

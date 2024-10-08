package com.aiddoru.dev.DTO.Auth;

import lombok.Data;

@Data
public class ConfirmSignupRequest {
    String username;
    String confirmCode;

}

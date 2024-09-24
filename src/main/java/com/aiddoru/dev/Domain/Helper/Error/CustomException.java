package com.aiddoru.dev.Domain.Helper.Error;

import com.aiddoru.dev.Domain.Enum.Error.CustomErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException{
    CustomErrorCode communityErrorCode;
}

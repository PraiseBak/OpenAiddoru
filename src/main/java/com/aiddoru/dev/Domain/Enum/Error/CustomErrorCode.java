package com.aiddoru.dev.Domain.Enum.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomErrorCode {
    /* 400 BAD_REQUEST : 잘못된 요청 */
    INVALID_RESOURCE(HttpStatus.BAD_REQUEST, "잘못된 요청입니다"),
    INVALID_IMAGE(HttpStatus.BAD_REQUEST, "유효하지 않은 이미지입니다"),

    /* 401 UNAUTHORIZED : 인증되지 않은 사용자 */
    INVALID_AUTH_TOKEN(HttpStatus.UNAUTHORIZED, "권한 정보가 없는 토큰입니다."),

    /* 404 NOT_FOUND : Resource를 찾을 수 없음 */
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당하는 정보를 찾을 수 없습니다"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당하는 유저를 찾을 수 없습니다"),

    /* 409 : CONFLICT : Resource의 현재 상태와 충돌. 보통 중복된 데이터 존재 */
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "데이터가 이미 존재합니다."),
    DUPLICATE_USER(HttpStatus.CONFLICT, "중복된 유저입니다"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다"),
    DUPLICATE_USER_INFO(HttpStatus.CONFLICT, "중복된 유저와 이메일입니다"),
    REQUEST_TIME_PASSED(HttpStatus.BAD_REQUEST,"요청 시간이 지났습니다. 다시 요청해주세요.");



    private final HttpStatus httpStatus;
    private String message;

    public CustomErrorCode modifyStr(String str){
        message = str;
        return this;

    }
}
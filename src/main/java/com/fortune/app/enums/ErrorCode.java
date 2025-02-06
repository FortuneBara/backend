package com.fortune.app.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "요청하신 정보를 확인해주세요."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    HAS_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    HAS_NICKNAME(HttpStatus.BAD_REQUEST, "중복된 닉네임입니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "요청하신 정보를 확인해주세요.)"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "해당 요청 정보를 찾을 수 없습니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 필요합니다.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}

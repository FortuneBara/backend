package com.fortune.app.common.exception;

import com.fortune.app.enums.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ErrorResponse {
    private final String message;
    private final List<String> errors;

    public static ErrorResponse of(ErrorCode errorCode, List<String> errors) {
        return new ErrorResponse(errorCode.getMessage(), errors);
    }
}

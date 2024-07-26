package com.spring.global.error.Exception;

import com.spring.global.error.ErrorCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

import java.text.MessageFormat;

@Getter
public class CustomAuthenticationException extends AuthenticationException {
    private final ErrorCode errorCode;
    private final String message;

    // 생성자: ErrorCode, String, 예외 클래스
    public CustomAuthenticationException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.message = errorCode.getMessage();
    }

    public CustomAuthenticationException(ErrorCode errorCode, String... message) {
        super(MessageFormat.format(errorCode.getMessage(), message));
        this.errorCode = errorCode;
        this.message = MessageFormat.format(errorCode.getMessage(), message);
    }

    // 생성자: ErrorCode, int, 예외 클래스
    public CustomAuthenticationException(ErrorCode errorCode, int intValue) {
        super(MessageFormat.format(errorCode.getMessage(), intValue));
        this.errorCode = errorCode;
        this.message = MessageFormat.format(errorCode.getMessage(), intValue);
    }
}

package com.spring.global.error.Exception;

import com.spring.global.error.ErrorCode;
import lombok.Getter;

import java.text.MessageFormat;


@Getter
public class CustomIllegalArgumentException extends IllegalArgumentException {
    private final ErrorCode errorCode;

    public CustomIllegalArgumentException(ErrorCode errorCode, int cnt) {
        super(MessageFormat.format(errorCode.getMessage(), cnt));
        this.errorCode = errorCode;
    }

    public CustomIllegalArgumentException(ErrorCode errorCode, String... messages) {
        super(MessageFormat.format(errorCode.getMessage(), messages));
        this.errorCode = errorCode;
    }

    public CustomIllegalArgumentException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;

    }
}

package com.spring.global.error;

import com.spring.global.error.Exception.CustomIllegalArgumentException;
import com.spring.global.helper.LoggingBuilder;
import com.spring.global.helper.LoggingBuilder.LogColors;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.Collections;
import java.util.List;

/**
 * 컨트롤러에서 발생하는 모든 예외를 전역적으로 관리하는 클래스
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler {

    private final LoggingBuilder loggingBuilder;


    /**
     * [Exception] 접근이 허용되지 않을 떄
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException exception) {
        loggingBuilder.append(LogColors.RED, "[{}.handleAccessDeniedException] : {}",
                this.getClass().getSimpleName(), exception);
        final ErrorResponse response = ErrorResponse.of(ErrorCode.UNAUTHORIZED_MEMBER);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    /**
     * [Exception] controller에서 전달한 값이 유효성을 통과하지 못했을 때
     *
     * @param exception
     * @return
     */
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(Exception exception) {
        BindingResult bindingResult = getBindingResult(exception);
        ErrorCode errorCode = ErrorCode.NOT_VALID_ERROR;
        final ErrorResponse response = ErrorResponse.of(errorCode, bindingResult);
        loggingBuilder.append(LogColors.RED, "[{}.handleMethodArgumentNotValidException] {}", this.getClass().getSimpleName(), response.toString());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    private BindingResult getBindingResult(Exception exception) {
        if (exception instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) exception).getBindingResult();
        } else if (exception instanceof BindException) {
            return ((BindException) exception).getBindingResult();
        }
        return null;
    }

    /**
     * [Exception] Controller에서 @RequestParam 에서 필수값 검증
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException exception) {
        loggingBuilder.append(LogColors.RED, "[{}.handleMissingServletRequestParameterException] : {}",
                this.getClass().getSimpleName());
        ErrorCode errorCode = ErrorCode.NOT_VALID_ERROR;
        String field = exception.getParameterName();
        List<ErrorResponse.CustomFieldError> resultList = Collections.singletonList(
                ErrorResponse.CustomFieldError.builder().field(field).message(errorCode.getMessage()).build());
        final ErrorResponse response = ErrorResponse.of(errorCode, resultList);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] 잘못된 서버 요청일 경우 발생한 경우
     *
     * @param exception HttpClientErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpClientErrorException.class)
    protected ResponseEntity<ErrorResponse> handleHttpClientErrorException(HttpClientErrorException exception
    ) {
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        loggingBuilder.append(LogColors.RED, "[{}.handleHttpClientErrorException] : {}",
                this.getClass().getSimpleName());
        exception.printStackTrace();
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] 없는 파일 / 경로를 요청할 때
     *
     * @param exception NoResourceFoundException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException exception) {
        ErrorCode errorCode = ErrorCode.NOT_FOUND;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        loggingBuilder.append(LogColors.RED, "[{}.handleNoResourceFoundException] : {}",
                this.getClass().getSimpleName(), response.toString());
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] 지원하지 않은 HTTP method 호출 할 경우 발생
     *
     * @param exception HttpRequestMethodNotSupportedException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException exception) {
        loggingBuilder.append(LogColors.RED, "[{}.handleHttpRequestMethodNotSupportedException] : {}",
                this.getClass().getSimpleName(), exception);
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] 필수 헤더를 제시하지 않을 경우
     *
     * @param exception MissingRequestHeaderException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(MissingRequestHeaderException.class)
    protected ResponseEntity<ErrorResponse> handleMissingRequestHeaderException(
            MissingRequestHeaderException exception) {
        loggingBuilder.append(LogColors.RED, "[{}.handleMissingRequestHeaderException] : {}",
                this.getClass().getSimpleName(), exception);
        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] Rest template 예외 전달
     *
     * @param exception HttpServerErrorException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(HttpServerErrorException.class)
    protected ResponseEntity<ErrorResponse> handleHttpServerErrorException(
            HttpServerErrorException exception) {
        loggingBuilder.append(LogColors.RED, "[{}.handleHttpServerErrorException] : {}",
                this.getClass().getSimpleName());
        exception.printStackTrace();
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] DB 에러관련 오류들
     *
     * @param exception DataAccessException
     * @return ResponseEntity<ErrorResponse>
     */
    @ExceptionHandler(DataAccessException.class)
    protected ResponseEntity<ErrorResponse> handleDataAccessException(DataAccessException exception,
                                                                      HttpServletRequest request, WebRequest webRequest) {
        loggingBuilder.append(LogColors.RED, "[{}.handleDataAccessException] ", this.getClass().getSimpleName());
        exception.printStackTrace();
        ErrorCode errorCode = determineErrorCode(exception);
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    private ErrorCode determineErrorCode(DataAccessException exception) {
        if (exception instanceof BadSqlGrammarException) {
            return ErrorCode.DB_BAD_SQL_GRAMMAR;
        } else if (exception instanceof InvalidResultSetAccessException) {
            return ErrorCode.DB_INVALID_RESULT_SET_ACCESS;
        } else if (exception instanceof DuplicateKeyException) {
            return ErrorCode.DB_DUPLICATE_KEY;
        } else if (exception instanceof DataIntegrityViolationException) {
            return ErrorCode.DB_DATA_INTEGRITY_VIOLATION;
        } else if (exception instanceof DataAccessResourceFailureException) {
            return ErrorCode.DB_DATA_ACCESS_RESOURCE_FAILURE;
        } else if (exception instanceof CannotAcquireLockException) {
            return ErrorCode.DB_CANNOT_ACQUIRE_LOCK_EXCEPTION;
        } else if (exception instanceof DeadlockLoserDataAccessException) {
            return ErrorCode.DB_DEADLOCK_LOSER_DATA_ACCESS;
        } else if (exception instanceof CannotSerializeTransactionException) {
            return ErrorCode.DB_CANNOT_SERIALIZE_TRANSACTION;
        } else {
            return ErrorCode.DB_EXCEPTION;
        }
    }

    /**
     * [Exception]
     * 스프링 시큐리티 예외
     *
     * @param // exception UserPasswordException
     */
//    @ExceptionHandler(CustomAuthenticationException.class)
//    protected ResponseEntity<ErrorResponse> handleCustomAuthenticationException(CustomAuthenticationException exception) {
//        loggingBuilder.append(LogColors.RED, "[{}.handleCustomAuthenticationException] : {}",
//                this.getClass().getSimpleName(),
//                exception);
//        ErrorCode errorCode = exception.getErrorCode();
//        final ErrorResponse response = ErrorResponse.of(errorCode);
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(AppConst.AUTHENTICATION_STATUS, writeAuthenticateState("Access to the site",
//                errorCode.getHeaderMessage()));
//
//        return new ResponseEntity<>(response, headers, errorCode.getHttpStatus());
//    }
    private String writeAuthenticateState(String realm, String error) {
        return "Basic realm=\"" + realm + "\", error=\"" + error + "\"";
    }

    /**
     * [Exception]
     * 비밀번호 변경 로직과 관련한 에러 발생시 (스프링 시큐리티를 벗어난 로직)
     *
     * @param exception UserPasswordException
     */
    @ExceptionHandler(CustomIllegalArgumentException.class)
    protected ResponseEntity<ErrorResponse> handleUserPasswordException(CustomIllegalArgumentException exception) {
        loggingBuilder.append(LogColors.RED, "[{}.handleUserPasswordException] : {}",
                this.getClass().getSimpleName(),
                exception);
        ErrorCode errorCode = exception.getErrorCode();
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }

    /**
     * [Exception] AuthenticationException 인증관련 예외
     * SecurityExceptionEntryPoint에서 처리 할 수 있게 전달
     *
     * @param exception AuthenticationException
     * @return
     */
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(Exception exception, WebRequest webRequest) {
        loggingBuilder.append(LogColors.RED, "[{}.handleAuthenticationException] : {}", this.getClass().getSimpleName(),
                exception);
        throw (AuthenticationException) exception;
    }

    /**
     * [Exception] 위에서 처리되지 않은 모든 에러
     * 해당 에러는 디비에 저장
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception, WebRequest webRequest) {
        loggingBuilder.append(LogColors.RED, "[{}.handleException]", this.getClass().getSimpleName());

        exception.printStackTrace();
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        final ErrorResponse response = ErrorResponse.of(errorCode);
        return new ResponseEntity<>(response, errorCode.getHttpStatus());
    }
}

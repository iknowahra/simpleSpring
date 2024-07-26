package com.spring.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.global.helper.AppConst;
import com.spring.global.error.ErrorCode;
import com.spring.global.error.ErrorResponse;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.helper.LoggingBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <pre>
 *     security filter exception handler
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SecurityExceptionEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;
    private final LoggingBuilder loggingBuilder;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        loggingBuilder.append(LoggingBuilder.LogColors.WHITE, "[{}.commence] {}", this.getClass().getSimpleName(), authException);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.UNAUTHORIZED.value()); // 401 Status code

        String error = "token_unknownProblem";

        if (authException instanceof CustomAuthenticationException) {
            ErrorCode errorCode = ((CustomAuthenticationException) authException).getErrorCode();
            error = errorCode.getHeaderMessage();
        }

        response.setHeader(AppConst.AUTHENTICATION_STATUS, writeAuthenticateState("Access to the site",
                error));

        // final ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        objectMapper.writeValue(response.getOutputStream(), writeBody(error, authException));
    }

    private String writeAuthenticateState(String realm, String error) {
        return "Basic realm=\"" + realm + "\", error=\"" + error + "\"";
    }

    private Object writeBody(String error, Exception ex) {
        return new ErrorResponse(error, ex.getMessage());
    }

}

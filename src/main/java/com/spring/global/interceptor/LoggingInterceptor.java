package com.spring.global.interceptor;

import com.spring.global.helper.LoggingBuilder;
import com.spring.global.helper.LoggingBuilder.LogColors;
import com.spring.global.utils.SecurityUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoggingInterceptor implements HandlerInterceptor {
    private final LoggingBuilder loggingBuilder;

    // 인터셉터에서 처리하지 않을 경로
    private boolean ignorePath(HttpServletRequest request) {
        return request.getRequestURI().startsWith("/v3/api-docs")
                || request.getRequestURI().startsWith("/swagger-ui");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (ignorePath(request)) {
            // 로그를 남기지 않음
        } else {
            // 요청 전 처리 작업
            loggingBuilder.appendTitle(LogColors.WHITE, "-- Interceptor START ");

            // 현재시각
            LocalDateTime now = LocalDateTime.now(); // 현재 날짜와 시간을 가져옴
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss.SSS"); // 포맷 지정
            String formattedDateTime = now.format(formatter);// 포맷 적용하여 문자열로 변환
            loggingBuilder.append(LogColors.WHITE, "# loggingTime: {} ", formattedDateTime);


            // sysCntnIp 요청IP주소
            String remoteAddr = request.getRemoteAddr();
            loggingBuilder.append(LogColors.WHITE, "# sysCntnIp: {} ", remoteAddr);

            // 사용자 id / 사용자 이름
            if (SecurityUtil.getUserId() != null) {
                loggingBuilder.append(LogColors.WHITE, "# userId: {} ", SecurityUtil.getUserId());
                loggingBuilder.append(LogColors.WHITE, "# userNm: {} ", SecurityUtil.getUserVO().getGvUserNm());
            }
        }

        // 요청 계속 진행
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        if (ignorePath(request)) {
            // 로그를 남기지 않음
        } else {
            // 요청 처리 완료 후 처리 작업
            loggingBuilder.appendTitle(LogColors.WHITE, "-- Interceptor END ");
            // 로그 콘솔 출력
            log.info(loggingBuilder.getConsoleLogs()); // Console에 로그 출력

        }
    }


}

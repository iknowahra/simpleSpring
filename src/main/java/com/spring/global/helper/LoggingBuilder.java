package com.spring.global.helper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * <pre>
 * HTTP 요청 범위에서 사용되는 로깅을 생성 빌더
 *
 * @author [Reading-snail](https://github.com/Reading-Snail)
 * </pre>
 */
@Component
@Getter
@Setter
@SuppressWarnings("null")
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class LoggingBuilder {

    private static final int MAX_LOG_LENGTH = 115; // 로깅 한 줄 최대 길이

    private final StringBuilder loggingBuilder;

    public LoggingBuilder() {
        this.loggingBuilder = new StringBuilder();
    }

    public void append(String log) {
        loggingBuilder.append(System.lineSeparator()).append(log);
    }

    public LoggingBuilder append(LogColors logColor, String message, Object... args) {
        try {
            for (Object arg : args) {
                message = message.replaceFirst("\\{\\}", arg != null ? arg.toString() : "null");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 색상을 정해줌
        String colorCode = logColor.getColorCode();
        message = colorCode + message + LogColors.RESET.getColorCode();

        loggingBuilder.append(System.lineSeparator()).append(message);
        return this;
    }

    public LoggingBuilder appendTitle(LogColors logColor, String message, Object... args) {
        for (Object arg : args) {
            message = message.replaceFirst("\\{\\}", arg != null ? arg.toString() : "null");
        }

        // 추가 로직: 메시지의 길이를 일정하게 맞추고 남은 부분을 '-'로 채움
        int messageLength = message.length();
        if (messageLength < MAX_LOG_LENGTH) {
            int paddingLength = MAX_LOG_LENGTH - messageLength;
            StringBuilder paddedMessage = new StringBuilder(message);
            for (int i = 0; i < paddingLength; i++) {
                paddedMessage.append("-");
            }
            message = paddedMessage.toString();
        }
        // 색상을 정해줌
        String colorCode = logColor.getColorCode();
        message = colorCode + message + LogColors.RESET.getColorCode();

        loggingBuilder.append(System.lineSeparator()).append(message);
        return this;
    }

    public String getConsoleLogs() {
        return loggingBuilder.toString();
    }

    public String getDataBaseLogs() {
        // Console에 폰트 색상 설정을 제거하는 로직
        LogColors[] logColorsArray = LogColors.values();
        for (LogColors color : logColorsArray) {
            String oldStr = color.getColorCode();
            int index = loggingBuilder.indexOf(oldStr); // oldStr의 첫 번째 위치 찾기
            while (index != -1) {
                loggingBuilder.replace(index, index + oldStr.length(), ""); // oldStr을 newStr로 대체
                index = loggingBuilder.indexOf(oldStr, index + "".length()); // 다음 oldStr 위치 찾기
            }
        }
        return loggingBuilder.toString();
    }

    // Console에 사용할 수 있는 색상 설정
    public static enum LogColors {
        BLACK("\u001B[30m"), // 검정
        RED("\u001B[31m"), // 빨강
        GREEN("\u001B[32m"), // 초록
        YELLOW("\u001B[33m"), // 노랑
        BLUE("\u001B[34m"), // 파랑
        MAGENTA("\u001B[35m"), // 자주
        CYAN("\u001B[36m"), // 시안
        WHITE("\u001B[37m"), // 흰색
        RESET("\u001B[0m"); // 초기화

        private final String colorCode;

        LogColors(String colorCode) {
            this.colorCode = colorCode;
        }

        public String getColorCode() {
            return colorCode;
        }
    }

}
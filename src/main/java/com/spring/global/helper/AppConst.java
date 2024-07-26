package com.spring.global.helper;

public class AppConst {
    // 인증/인가
    public static final String BEARER_PREFIX = "Bearer "; // 토큰 전치사
    public static final int PASSWORD_EXPIRE_DURATION_DAYS = 45; // 비밀번호 만료일
    public static final int PASSWORD_EXPIRE_CHECK_DAYS = 10; // 비밀번호 만료예정 알림 기준일
    public static final int LOGIN_FAIL_COUNT_LIMIT = 5; // 비밀번호 틀린 횟수 제한
    public static final long ACCOUNT_LOCK_DURATION_DAYS = 45; // 미사용 계정 자동 잠금 기준일

    public static final String AUTHENTICATION_STATUS = "Authenticate-Status"; // 인증/인가 상태 반환 헤더

    public static final String MENU_ID = "Menu-Id"; // 화면에서 메뉴ID를 전달해주는 헤더
    public static final String LOG_YN = "Log-Yn"; // 화면의 로그기록 여부를 전달해주는 헤더

    private AppConst() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

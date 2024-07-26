package com.spring.global.security;

import com.spring.global.helper.AppConst;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class PasswordManager {
    private final int loginFailCntLimit = AppConst.LOGIN_FAIL_COUNT_LIMIT;

    public static boolean isPasswordPlanToExpired(String pwChgDtm) {
        long pwdMonthChkCnt = calculatePwExpireRemainDate(pwChgDtm);
        return pwdMonthChkCnt < AppConst.PASSWORD_EXPIRE_CHECK_DAYS && pwdMonthChkCnt >= 0;
    }

    public static boolean isPasswordExpired(String pwChgDtm) {
        long remainDate = calculatePwExpireRemainDate(pwChgDtm);
        return remainDate <= 0;
    }

    public static boolean isAccountNotUsed(String lastLgnDtm) {
        LocalDate currentDate = LocalDate.now();
        LocalDate dtLastLgnDtm = LocalDate.parse(lastLgnDtm, DateTimeFormatter.ofPattern("yyyyMMdd"));
        long accountNotUsedCnt = ChronoUnit.DAYS.between(dtLastLgnDtm, currentDate);

        return accountNotUsedCnt > AppConst.ACCOUNT_LOCK_DURATION_DAYS;
    }

    public static long calculatePwExpireRemainDate(String pwChgDtm) {
        if (pwChgDtm == null) return -1;

        LocalDate currentDate = LocalDate.now();

        LocalDate chgDtm = LocalDate.parse(pwChgDtm, DateTimeFormatter.ofPattern("yyyyMMdd"));
        LocalDate endDate = chgDtm.plusDays(AppConst.PASSWORD_EXPIRE_DURATION_DAYS);

        return ChronoUnit.DAYS.between(currentDate, endDate);
    }
}

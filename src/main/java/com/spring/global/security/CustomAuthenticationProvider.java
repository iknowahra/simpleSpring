package com.spring.global.security;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.helper.AppConst;
import com.spring.loginMvc.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final LoginService loginService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userId = authentication.getName();
        String userPw = authentication.getCredentials().toString();

        // 사용자 정보 가져오기
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(userId);


        // 계정 미사용 기간 45일 초과 확인
        if (user.isAccountNotUsed()) {
            loginService.updateLockYNtoY(userId); // 계정 잠금
            throw new CustomAuthenticationException(ErrorCode.INACTIVE_USER);
        }

        // 임시비밀번호상태 확인
        if (user.isTemporaryPassword()) {
            throw new CustomAuthenticationException(ErrorCode.TEMPORARY_PASSWORD_CHANGE_REQUIRED);
        }

        // 계정 잠금 여부 확인
        if (!user.isAccountNonLocked()) {
            // 계정 미사용 기간 45일 초과일 경우
            if (user.isAccountNotUsed())
                throw new CustomAuthenticationException(ErrorCode.INACTIVE_USER);
            // 비밀번호 연속 오류일 경우
            throw new CustomAuthenticationException(ErrorCode.LOCKED_USER, user.getLgnFailCnt());
        }
        if (!passwordEncoder.matches(userPw, user.getPassword())) { // 비밀번호가 일치하지 않을 경우
            if (user.getUserDTO().getLgnFailCnt() < AppConst.LOGIN_FAIL_COUNT_LIMIT) {
                loginService.updateLgnFailCntByPlusOne(userId); // 비밀번호 실패 횟수 1 증가
                throw new CustomAuthenticationException(ErrorCode.PASSWORD_N_TIMES_MISMATCH, user.getLgnFailCnt() + 1);
            } else {
                loginService.updateLockYNtoY(userId); // 계정 잠금
                throw new CustomAuthenticationException(ErrorCode.LOCKED_USER, user.getLgnFailCnt());
            }
        } else {
            // 비밀번호 일치할 경우
            loginService.updateLogFailCntToZero(userId); // 비밀번호 실패 횟수 0으로 초기화
        }

        // 비밀번호 만료
        if (user.isAccountNonExpired()) {
            throw new CustomAuthenticationException(ErrorCode.EXPIRED_PASSWORD_CHANGE_REQUIRED);
        }
        // 비밀번호만료예정상태
        if (user.isPasswordPlanToExpired()) {
            long remainDate = PasswordManager.calculatePwExpireRemainDate(user.getUserDTO().getRegDtm());
            user.setPwExpireRemainDate(remainDate);
        }

        // 인증 성공 시 처리
        return new UsernamePasswordAuthenticationToken(user, userPw, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}

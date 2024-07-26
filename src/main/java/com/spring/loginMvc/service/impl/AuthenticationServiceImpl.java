package com.spring.loginMvc.service.impl;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.security.CustomUserDetails;
import com.spring.global.security.JwtTokenProvider;
import com.spring.loginMvc.dto.LoginDTO;
import com.spring.loginMvc.service.AuthenticationService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public String login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserId(),
                loginDTO.getUserPw());
        Authentication authentication = authenticationManager.authenticate(authToken);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // userDetails에 추가 데이터 입력
        userDetails.setLangCd(loginDTO.getLangCd()); // 언어코드 주입 (e.g. langCd)

        String accessToken = tokenProvider.createAccessToken(authentication); // Access 토큰 생성
        String refreshToken = tokenProvider.createRefreshToken(authentication); // Refresh 토큰 생성

        // refresh 토큰 저장
        storeRefreshToken(accessToken, refreshToken);

        return accessToken;
    }

    @Override
    public String refresh(String accessToken) {

        // 세션에 저장된 Refresh 토큰 불러오기
        String refreshToken = getRefreshToken(accessToken);
        if (refreshToken == null) {// 세션에 저장된 Refresh 토큰이 없을 경우, 예외처리
            throw new CustomAuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }

        // Refresh 토큰 불러오기
        if (!tokenProvider.isValidToken(refreshToken)) {// Refresh 토큰이 만료되었을 경우, 예외처리
            throw new CustomAuthenticationException(ErrorCode.NOT_VALID_TOKEN);
        }
        // 위의 유효성 검사 완료 후 이상 없을 경우, Access 토큰 Refresh 토큰 재발급하여 Access 토큰만 화면으로 반환
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        String newAccessToken = tokenProvider.createAccessToken(authentication); // Access 토큰 생성 로직 호출
        String newRefreshToken = tokenProvider.createRefreshToken(authentication); // Refresh 토큰 생성 로직 호출

        removeRefreshToken(accessToken); // 기존 Access 토큰, Refresh 토큰 세션에 제거
        storeRefreshToken(newAccessToken, newRefreshToken); // 새로운 Access 토큰, Refresh 토큰 세션에 저장

        return newAccessToken;
    }

    // 필요에 따라 세션, DB, Redis에서 Refresh 토큰을 불러오도록 구현
    private String getRefreshToken(String accessToken) {
        HttpSession session = getSession();
        String refreshToken = (String) session.getAttribute(accessToken);
        return refreshToken;
    }

    // 필요에 따라 세션, DB, Redis에서 저장하도록 구현
    private void storeRefreshToken(String accessToken, String refreshToken) {
        HttpSession session = getSession();
        session.setAttribute(accessToken, refreshToken);
    }

    // 필요에 따라 세션, DB, Redis에서 제거하도록 구현
    private void removeRefreshToken(String accessToken) {
        HttpSession session = getSession();
        session.removeAttribute(accessToken);
    }

    private HttpSession getSession() {
        return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getSession();
    }
}

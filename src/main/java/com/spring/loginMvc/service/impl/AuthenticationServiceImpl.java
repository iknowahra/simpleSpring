package com.spring.loginMvc.service.impl;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.security.CustomUserDetails;
import com.spring.global.security.JwtTokenProvider;
import com.spring.loginMvc.dto.LoginDTO;
import com.spring.loginMvc.dto.TokenDTO;
import com.spring.loginMvc.service.AuthenticationService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.getUserId(),
                loginDTO.getUserPw());
        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // userDetails에 추가 데이터 입력
        userDetails.setLangCd(loginDTO.getLangCd()); // 언어코드 주입 (e.g. langCd)

        String accessToken = tokenProvider.createAccessToken(authentication); // Access 토큰 생성
        String refreshToken = tokenProvider.createRefreshToken(authentication); // Refresh 토큰 생성

        return new TokenDTO(accessToken, refreshToken, true);
    }

    @Override
    public TokenDTO refresh(final String refreshToken) {
        if (refreshToken == null)
            throw new CustomAuthenticationException(ErrorCode.NOT_VALID_TOKEN);

        if (!tokenProvider.isValidToken(refreshToken)) {// Refresh 토큰이 만료되었을 경우, 예외처리
            throw new CustomAuthenticationException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        // 위의 유효성 검사 완료 후 이상 없을 경우, Access 토큰 Refresh 토큰 재발급하여 Access 토큰만 화면으로 반환
        Authentication authentication = tokenProvider.getAuthentication(refreshToken);

        String newAccessToken = tokenProvider.createAccessToken(authentication); // Access 토큰 생성 로직 호출
        String newRefreshToken = tokenProvider.createRefreshToken(authentication); // Refresh 토큰 생성 로직 호출

        TokenDTO tokenDto = new TokenDTO(newAccessToken, newRefreshToken);
        return tokenDto;
    }

    @Override
    public String getUserId(final String token){
        Claims claims = tokenProvider.getClaims(token);
        String userId = (String) claims.get("userId");
        return userId;
    }

    @Override
    public String removePrefixToken(String token){
        return tokenProvider.removeBearerPrefix(token);
    }

}

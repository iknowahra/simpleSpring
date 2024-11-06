package com.spring.global.security;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.loginMvc.dto.UserDTO;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * <pre>
 *     토큰 생성과 유효성 검증을 담당
 * </pre>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsService service;
    private String TOKEN_HEADER_PREFIX = "Bearer ";
    @Value("${simplespring.jwt.signing-Key}")
    private String signingKey;
    @Value("${simplespring.jwt.access-token-valid-period}")
    private long accessTokenValidPeriod;
    @Value("${simplespring.jwt.refresh-token-valid-period}")
    private long refreshTokenValidPeriod;

    private void addAuthenticationToClaims(final Authentication authentication, Claims claims) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        UserDTO userVO = userDetails.getUserDTO();
        claims.put("langCd", userDetails.getLangCd()); // gvLangCd
        claims.put("userNm", userVO.getUserNm()); // gvUserNm
        claims.put("mailAddr", userVO.getMailAddr()); // gvMailAddr
        claims.put("telNo", userVO.getTelNo()); // gvTelNo
        claims.put("deptCd; ", userVO.getDeptCd()); // gvDeptCd
        claims.put("posiCd", userVO.getPosiCd()); // gvPosiCd
        claims.put("dutyCd; ", userVO.getDeptCd()); // gvDutyCd
        claims.put("mgrUserI", userVO.getMgrUserId()); // gvMgrUserI
    }

    public String createAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenValidPeriod, true);
    }

    public String createRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshTokenValidPeriod, false);
    }

    public String generateToken(Authentication authentication, long tokenValidPeriod, boolean isAccessToken) {
        Claims claims = Jwts.claims();// userId 주입
        claims.put("userId", authentication.getName());


        // 추가적인 파라미터 주입
        if (isAccessToken) addAuthenticationToClaims(authentication, claims);

        long now = new Date().getTime();
        Date expiryDate = new Date(now + tokenValidPeriod);

        return Jwts.builder()
                .setSubject(authentication.getName())
                .setClaims(claims) // 여기서는 claims 객체를 직접 사용해야 합니다.
                .setIssuedAt(new Date(now))
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, signingKey)
                .compact();
    }

    public Authentication getAuthentication(String token) {

        Claims claims = getClaims(token);
        String userId = (String) claims.get("userId"); // userId 추출
        String langCd = (String) claims.get("langCd");

        // refresh 토큰의 경우 userId만 가지고 잇다... 그래서 에러가 남..
        CustomUserDetails userDetails = (CustomUserDetails) service.loadUserByUsername(userId);
        userDetails.setLangCd(langCd);

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    public Claims getClaims(String token) {
        Claims body = null;
        try {
            body = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) { // 토큰 만료
            throw new CustomAuthenticationException(ErrorCode.TOKEN_EXPIRED);
        } catch (SignatureException e) { // 토큰 서명 불일치
            throw new CustomAuthenticationException(ErrorCode.TOKEN_SIGN_NOT_MATCH);
        } catch (MalformedJwtException e) { // 인코딩 불가
            throw new CustomAuthenticationException(ErrorCode.TOKEN_PROBLEM, e.getMessage());
        } catch (UnsupportedJwtException e) { // 지원되지 않는 토큰
            throw new CustomAuthenticationException(ErrorCode.TOKEN_UNSUPPORTED, e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new CustomAuthenticationException(ErrorCode.TOKEN_PROBLEM, e.getMessage());
        }
        return body;
    }

    /**
     * 토큰 유효여부 확인
     *
     * @param token
     * @return 유효할 경우 true , 만료되었을 경우 false
     */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ex) { // 만료된 토큰
            return false;
        }
    }

    /**
     * 액세스 토큰의 접두사 제거
     *
     * @param bearerToken
     * @return
     */
    public String removeBearerPrefix(String bearerToken) {
        if (StringUtils.startsWithIgnoreCase(bearerToken, TOKEN_HEADER_PREFIX)) {
            return StringUtils.replace(bearerToken, TOKEN_HEADER_PREFIX, "");
        }
        return null;
    }

}

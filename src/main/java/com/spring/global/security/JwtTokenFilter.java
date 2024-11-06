package com.spring.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 토큰 검증 로직
        String token = resolveToken(request);
        Authentication authentication = null;
        try {
            authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response); // 다음 필터로 이동
        } catch (AuthenticationException exception) {
            // AuthenticationException은 AuthenticationEntryPoint.commence()으로 관리
            handleException(request, response, exception);
        } catch (Exception e) {
            log.error("[{}.doFilterInternal]", this.getClass().getSimpleName(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void handleException(HttpServletRequest request, HttpServletResponse response,
                                 AuthenticationException exception)
            throws IOException, ServletException {
        //SecurityContextHolder.clearContext();
        authenticationEntryPoint.commence(request, response, exception);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        // 해당 경로에는 트리거되지 않도록 필터를 구성
        return request.getServletPath().equals("/login")
                || request.getServletPath().equals("/logout")
                || request.getServletPath().equals("/refresh")
                || request.getServletPath().equals("/change-password")
                || request.getServletPath().equals("/login/languages")
                || request.getServletPath().equals("/swagger-ui/index.html")
                || request.getServletPath().startsWith("/swagger-ui")
                || request.getServletPath().startsWith("/v3/api-docs")
                || request.getServletPath().startsWith("/api-docs/");
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        return jwtTokenProvider.removeBearerPrefix(bearerToken);
    }
}


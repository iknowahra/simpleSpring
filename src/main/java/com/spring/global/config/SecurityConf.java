package com.spring.global.config;

import com.spring.global.security.CustomLogoutSuccessHandler;
import com.spring.global.security.JwtTokenFilter;
import com.spring.global.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import static jakarta.servlet.DispatcherType.ERROR;
import static jakarta.servlet.DispatcherType.FORWARD;

/**
 * Spring Security를 구성하여 JWT 토큰 기반의 인증 및 권한 부여를 활성화하고, HTTP 보안 설정을 제공
 */

@Configuration
@EnableWebSecurity // Spring Security 활성화
@RequiredArgsConstructor
public class SecurityConf {
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationEntryPoint exceptionHandler;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector).servletPath("/");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // JWT와 Browser Storage를 사용할 경우, CSRF를 보호할 필요가 없다는 것이 지배적인 의견
                .formLogin(login -> login.disable())
                .httpBasic(basic -> basic.disable())
                .addFilterBefore(new JwtTokenFilter(tokenProvider, exceptionHandler),
                        UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(handling -> handling.authenticationEntryPoint(exceptionHandler))
                .authorizeHttpRequests(requests -> requests
                        .dispatcherTypeMatchers(FORWARD, ERROR).permitAll()
                        .requestMatchers("/login", "/logout", "/login/languages", "/refresh",
                                "/change-password", "/swagger-ui/index.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .addHeaderWriter(new XXssProtectionHeaderWriter())
                        .xssProtection(xss -> xss
                                .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)// Spring Security에서 제공하는 X-XSS-Protection 헤더를 사용한 XSS 방어
                        ).contentSecurityPolicy(cps -> cps.policyDirectives("script-src 'self'")))// X-XSS-Protection이 지원되지 않는 구형 브라우저의 경우 CSP를 사용하여 보호
                .logout(logout -> logout
                        .logoutUrl("/logout") // 로그아웃 URL 설정
                        .logoutSuccessHandler(logoutSuccessHandler) // 커스텀 로그아웃 성공 핸들러 설정
                        .invalidateHttpSession(true) // HTTP 세션 무효화
                        .deleteCookies("JSESSIONID")) // 로그아웃 후 쿠키 삭제
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }

}
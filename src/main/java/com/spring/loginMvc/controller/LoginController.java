package com.spring.loginMvc.controller;

import com.spring.global.helper.AppConst;
import com.spring.global.helper.LangCode;
import com.spring.global.security.JwtTokenProvider;
import com.spring.loginMvc.dto.LoginDTO;
import com.spring.loginMvc.dto.PwChangeDTO;
import com.spring.loginMvc.service.AuthenticationService;
import com.spring.loginMvc.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("")
@RequiredArgsConstructor
@Tag(name = "Login API", description = "로그인, 토큰, 비밀번호 갱신")
public class LoginController {
    private final AuthenticationService authenticationService;
    private final LoginService loginService;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 로그인 요청
     */
    @PostMapping("/login")
    @Operation(summary = "로그인 요청", description = "")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {

        String accessToken = authenticationService.login(loginDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, accessToken);

        // Access 토큰 바디에 저장
        return ResponseEntity.ok().headers(headers).body(accessToken);
    }

    /**
     * Refresh 토큰을 통한 AccessToken 재발급 요청
     */
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "")
    public ResponseEntity<String> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {

        accessToken = jwtTokenProvider.removeBearerPrefix(accessToken); // Bearer 제거
        String newAccessToken = AppConst.BEARER_PREFIX + authenticationService.refresh(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, newAccessToken);

        return ResponseEntity.ok().headers(headers).body(newAccessToken);
    }

    /**
     * 로그인화면 언어코드 조회
     */
    @GetMapping("/login/languages")
    @Operation(summary = "로그인 화면 언어정보", description = "")
    public ResponseEntity<List<Map<String, String>>> getLangCdAndName() {
        return ResponseEntity.ok(LangCode.toList());
    }

    /**
     * 비밀번호 변경
     */
    @PatchMapping("/change-password")
    @Operation(summary = "비밀번호 변경", description = "")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PwChangeDTO pwChangeDTO) {
        loginService.changePassword(pwChangeDTO);
        return ResponseEntity.ok("OK");
    }

}

package com.spring.loginMvc.controller;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomIllegalArgumentException;
import com.spring.global.helper.AppConst;
import com.spring.global.helper.LangCode;
import com.spring.loginMvc.dto.LoginDTO;
import com.spring.loginMvc.dto.PwChangeDTO;
import com.spring.loginMvc.dto.TokenDTO;
import com.spring.loginMvc.service.AuthenticationService;
import com.spring.loginMvc.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@Tag(name = "Login API", description = "로그인, 토큰, 비밀번호 갱신")
public class LoginController {
    private final AuthenticationService authenticationService;
    private final LoginService loginService;
    private final RestTemplate restTemplate;
    private final DiscoveryClient discoveryClient;

    /**
     * 로그인 요청
     *
     * @param loginDTO
     * @return
     */
    @PostMapping("")
    @Operation(summary = "로그인 요청", description = "")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDTO) {
        TokenDTO authResponse = authenticationService.login(loginDTO);

        // refresh token 을 redis 서버로 보내 저장
        requestStoreTokenInRedis(loginDTO.getUserId(), authResponse);

        return ResponseEntity.ok().body(authResponse.getAccessToken());
    }

    /**
     * private
     * 레디스 서버에 토큰 정보 (액세스 / 리프레시) 저장
     *
     * @param userId
     * @param authResponse
     */
    private void requestStoreTokenInRedis(String userId, TokenDTO authResponse) {
        List<ServiceInstance> simpleRedis = discoveryClient.getInstances("SIMPLEREDIS");
        if (simpleRedis != null && !simpleRedis.isEmpty()) {
            String redisUri = simpleRedis.get(0).getUri().toString() + "/v1/" + userId + "/create-token";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<TokenDTO> requestBody = new HttpEntity<>(authResponse, headers);
            restTemplate.exchange(redisUri, HttpMethod.POST, requestBody, String.class);
        }
    }

    /**
     * 액세스 토큰 갱신
     *
     * @param accessToken
     * @return 갱신된 액세스 토큰 반환
     */
    @PostMapping("/refresh")
    @Operation(summary = "토큰 갱신", description = "")
    public ResponseEntity<String> refresh(@RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken) {
        if (accessToken == null) {
            throw new CustomIllegalArgumentException(ErrorCode.ACCESS_TOKEN_NOT_FOUND);
        }
        // 토큰 접두사 제거
        accessToken = authenticationService.removePrefixToken(accessToken);
        String refreshToken = requestRetrieveTokenInRedis(accessToken);// redis 서버에서 리프레시 토큰 가져옴.
        String userId = authenticationService.getUserId(refreshToken);
        log.error("⭐️️️️️️⭐️⭐️️️️️️⭐️⭐️️️️️️⭐️⭐️️️️️️⭐️⭐️️️️️️⭐️⭐️️️️️️⭐refresh :: userId ::{} :: token {}", userId, refreshToken);

        TokenDTO tokenDTO = authenticationService.refresh(refreshToken);

        // 갱신된 리프레시 토큰을 레디스 서버에 저장
        requestStoreTokenInRedis(userId, tokenDTO);

        HttpHeaders headers = new HttpHeaders();
        String newAccessToken = AppConst.BEARER_PREFIX + tokenDTO.getAccessToken();
        headers.add(HttpHeaders.AUTHORIZATION, newAccessToken);

        return ResponseEntity.ok().headers(headers).body(newAccessToken);
    }

    /**
     * private
     * 레디스 서버에서 만료된 액세스 토큰을 이용해서 리프레시 토큰 찾기
     *
     * @param accessToken
     * @return
     */
    private String requestRetrieveTokenInRedis(final String accessToken) {
        String refreshToken = null;
        List<ServiceInstance> simpleRedis = discoveryClient.getInstances("SIMPLEREDIS");
        log.info("💗💗💗💗💗💗💗💗{}", simpleRedis.toString());
        if (simpleRedis != null && !simpleRedis.isEmpty()) {
            String redisUri = simpleRedis.get(0).getUri().toString() + "/v1/refresh";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<String> requestBody = new HttpEntity<>(accessToken, headers);
            refreshToken = restTemplate.exchange(redisUri, HttpMethod.POST, requestBody, String.class).getBody();

            log.info("💗💗💗💗💗💗💗💗{}", requestBody.getBody());
            // refresh token이 없을 경우 예외 발생
            if (refreshToken == null) {
                throw new CustomIllegalArgumentException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
            }
        }
        return refreshToken;
    }

    /**
     * 로그인 화면 언어 정보
     *
     * @return List<Map < String, String>>
     */
    @GetMapping("/languages")
    @Operation(summary = "로그인 화면 언어정보", description = "")
    public ResponseEntity<List<Map<String, String>>> getLangCdAndName() {
        return ResponseEntity.ok(LangCode.toList());
    }

    /**
     * 비밀번호 변경
     *
     * @param pwChangeDTO
     * @return String (OK)
     */
    @PatchMapping("/change-password")
    @Operation(summary = "비밀번호 변경", description = "")
    public ResponseEntity<String> changePassword(@RequestBody @Valid PwChangeDTO pwChangeDTO) {
        loginService.changePassword(pwChangeDTO);
        return ResponseEntity.ok("OK");
    }

}

package com.spring.loginMvc.controller;

import com.spring.global.helper.ParamMap;
import com.spring.loginMvc.service.UserInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/userInfo")
@RequiredArgsConstructor
@Tag(name = "User 정보 API", description = "메뉴정보, 즐겨찾기, 권한, 공통코드")
public class UserInfoController {

    private final UserInfoService service;

    @GetMapping("/menu")
    @Operation(summary = "사용자 메뉴 조회")
    public ResponseEntity<?> retrieveUserMenuList() {
        Map paramMap = new ParamMap();
        log.info("🔑🔑🔑🔑🔑🔑🔑🔑🔑🔑🔑 paramMap : {}", paramMap);
        List menuListByUser = service.getMenuListByUser(paramMap);

        return ResponseEntity.ok(menuListByUser);
    }
}


package com.spring.global.utils;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.helper.CurrentUserVO;
import com.spring.global.security.CustomUserDetails;
import com.spring.loginMvc.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Context에서 자주 꺼내 쓰는 객체
 * getUserId()
 * getUserVO()
 */
@Slf4j
public class SecurityUtil {

    public static String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.error("🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥🔥authentication {}", authentication);
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof CustomUserDetails) {
                CustomUserDetails customUserDetails = (CustomUserDetails) principal;
                return customUserDetails.getUsername();
            }
        }
        return null; // 익명 사용자 또는 인증되지 않은 사용자
    }

    public static CurrentUserVO getUserVO() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
            UserDTO userDTO = customUserDetails.getUserDTO();
            return new CurrentUserVO(userDTO, customUserDetails.getLangCd());
        } else {
            throw new CustomAuthenticationException(ErrorCode.USER_NOT_FOUND);
        }
    }
}

package com.spring.global.security;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.helper.LoggingBuilder;
import com.spring.global.helper.LoggingBuilder.LogColors;
import com.spring.loginMvc.dao.LoginMapper;
import com.spring.loginMvc.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final LoginMapper loginMapper;

    private final LoggingBuilder loggingBuilder;

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        CustomUserDetails user = null;
        user = (CustomUserDetails) getUserInfo(userId);

        if (user == null) { // 사용자 정보를 찾을 수 없을 때
            loggingBuilder.append(LogColors.RED, "Query returned no results for user '" + userId + "'");
            throw new CustomAuthenticationException(ErrorCode.NOT_EXISTS_USER);
        }

        return user;
    }

    public UserDetails getUserInfo(String userId) {
        try {
            // 사용자 정보 조회
            UserDTO user = loginMapper.selectUser(userId);
            return new CustomUserDetails(user);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

}

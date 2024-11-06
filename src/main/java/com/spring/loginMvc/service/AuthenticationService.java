package com.spring.loginMvc.service;

import com.spring.loginMvc.dto.LoginDTO;
import com.spring.loginMvc.dto.TokenDTO;

public interface AuthenticationService {
    TokenDTO login(LoginDTO loginDTO);

    TokenDTO refresh(String refreshToken);

    String getUserId(String token);

    String removePrefixToken(String token);
}

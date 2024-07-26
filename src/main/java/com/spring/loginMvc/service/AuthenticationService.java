package com.spring.loginMvc.service;

import com.spring.loginMvc.dto.LoginDTO;

public interface AuthenticationService {
    String login(LoginDTO loginDTO);

    String refresh(String accessToken);
}

package com.spring.loginMvc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TokenDTO {
    private String accessToken;
    private String refreshToken;
    private boolean useYn;

    public TokenDTO(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.useYn = true;
    }

    public TokenDTO(String accessToken, String refreshToken, boolean useYn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.useYn = useYn;
    }
}

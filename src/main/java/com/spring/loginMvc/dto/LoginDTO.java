package com.spring.loginMvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Alias("LoginDTO")
public class LoginDTO {
    @Schema(example = "C19006")
    private String userId; // 사용자아이디
    @Schema(example = "1")
    private String userPw; // 비밀번호
    @Schema(example = "ko")
    private String langCd; // 언어코드
}
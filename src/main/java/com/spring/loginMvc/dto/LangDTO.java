package com.spring.loginMvc.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.apache.ibatis.type.Alias;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Alias("LangDTO")
public class LangDTO {
    @Schema(example = "ko")
    private String langCd; // 언어코드
    @Schema(example = "한글")
    private String langNm; // 언어국가
}

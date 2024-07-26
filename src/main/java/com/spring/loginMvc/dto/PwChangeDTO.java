package com.spring.loginMvc.dto;

import com.spring.global.helper.AuthDTO;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.Alias;

@Getter
@Setter
@ToString(callSuper = true)
@Alias("PwChangeDTO")
@Slf4j
public class PwChangeDTO extends AuthDTO {
    private String userId;
    private String currentPw;
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]*$", message = "비밀번호는 영문 대소문자, 숫자, 특수 문자 중 최소 하나를 포함해야 합니다.")
    private String newPw;

    private String confirmPw;

    @Hidden
    public boolean isPwNewAndPwConfirmMatches() {
        return this.newPw.equals(this.confirmPw);
    }

}


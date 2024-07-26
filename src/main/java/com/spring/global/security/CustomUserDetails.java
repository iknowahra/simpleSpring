package com.spring.global.security;

import com.spring.loginMvc.dto.UserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/*
 * Spring Security에서 관리하고 사용하는 사용자 정보
 *
 */
@Getter
@Setter
@Slf4j
public class CustomUserDetails implements UserDetails, CredentialsContainer {
    private final UserDTO userDTO;

    private String langCd; /* 언어코드 */
    private String accessIp; /* 접속IP */
    private long pwExpireRemainDate; /* 비번 만료일 */

    public CustomUserDetails(final UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    @Override
    public String getUsername() {
        return userDTO.getUserId();
    }

    @Override
    public String getPassword() {
        return userDTO.getUserPw();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public void eraseCredentials() {
        userDTO.setUserPw(null);
    }

    @Override
    public boolean isAccountNonExpired() {
        String lastLgnDtm = userDTO.getLastLgnDtm();
        if (lastLgnDtm == null || "".equals(lastLgnDtm)) return false;
        return PasswordManager.isPasswordExpired(userDTO.getPwChgDtm());
    }

    @Override
    public boolean isAccountNonLocked() {
        return !"Y".equals(userDTO.getLockYn());
    }

    public boolean isTemporaryPassword() {
        return "Y".equals(userDTO.getFrstLgnYn());
    }

    public boolean isAccountNotUsed() {
        String lastLgnDtm = userDTO.getLastLgnDtm();
        if (lastLgnDtm == null || "".equals(lastLgnDtm)) return true;
        return PasswordManager.isAccountNotUsed(lastLgnDtm);
    }

    public boolean isPasswordPlanToExpired() {
        String pwChgDtm = userDTO.getPwChgDtm();
        if (pwChgDtm == null || "".equals(pwChgDtm)) return true;
        return PasswordManager.isPasswordPlanToExpired(pwChgDtm);
    }

    public int getLgnFailCnt() {
        return userDTO.getLgnFailCnt();
    }

    @Override
    public boolean isEnabled() {
        log.info("isEnabled");
        return isAccountNonExpired() && isAccountNonExpired() && !isTemporaryPassword() && isAccountNotUsed();
    }
}
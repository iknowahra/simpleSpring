package com.spring.global.helper;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomIllegalArgumentException;
import com.spring.loginMvc.dto.UserDTO;
import lombok.Getter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;
import java.util.Objects;


@Getter
@ToString
@RequestScope
public final class CurrentUserVO implements Serializable {
    private final String gvUserId;
    private final String gvUserNm;
    private final String gvMailAddr;
    private final String gvTelNo;
    private final String gvDeptCd;
    private final String gvPosiCd;
    private final String gvDutyCd;
    private final String gvLangCd;
    private final String gvMgrUserId;
    @Value("${simple.spring.lang.code}")
    private String langCd; // 프로그램 기본 설정 언어

    public CurrentUserVO(UserDTO userDTO) {
        if (userDTO.getUserId() == null || "".equals(userDTO.getUserId()))
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_USER_DATA);
        this.gvUserId = userDTO.getUserId();
        this.gvUserNm = userDTO.getUserNm();
        this.gvMailAddr = userDTO.getMailAddr();
        this.gvTelNo = userDTO.getTelNo();
        this.gvDeptCd = userDTO.getDeptCd();
        this.gvPosiCd = userDTO.getPosiCd();
        this.gvDutyCd = userDTO.getDutyCd();
        this.gvMgrUserId = userDTO.getMgrUserId();
        this.gvLangCd = langCd;
    }

    public CurrentUserVO(UserDTO userDTO, final String langCd) {
        if (userDTO.getUserId() == null || "".equals(userDTO.getUserId()))
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_USER_DATA);
        this.gvUserId = userDTO.getUserId();
        this.gvUserNm = userDTO.getUserNm();
        this.gvMailAddr = userDTO.getMailAddr();
        this.gvTelNo = userDTO.getTelNo();
        this.gvDeptCd = userDTO.getDeptCd();
        this.gvPosiCd = userDTO.getPosiCd();
        this.gvDutyCd = userDTO.getDutyCd();
        this.gvMgrUserId = userDTO.getMgrUserId();
        this.gvLangCd = langCd;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        CurrentUserVO userVO = (CurrentUserVO) obj;
        return Objects.equals(gvUserId, userVO.gvUserId) && Objects.equals(gvMailAddr, userVO.getGvMailAddr());
    }
}

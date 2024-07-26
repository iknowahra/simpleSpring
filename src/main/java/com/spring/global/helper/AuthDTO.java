package com.spring.global.helper;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomIllegalArgumentException;
import jakarta.annotation.PreDestroy;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import static org.springframework.web.context.WebApplicationContext.SCOPE_REQUEST;


@ToString
@Getter
@Scope(value = SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
@Component
public abstract class AuthDTO {
    private String gvUserId;
    private String gvUserNm;
    private String gvMailAddr;
    private String gvTelNo;
    private String gvDeptCd;
    private String gvPosiCd;
    private String gvDutyCd;
    private String gvLangCd;
    private String gvMgrUserId;

    public void setUserVO(CurrentUserVO userVO) {
        if (userVO == null || userVO.getGvUserId() == null || "".equals(userVO.getGvUserId())) {
            clearUserVO();
            throw new CustomIllegalArgumentException(ErrorCode.INVALID_USER_DATA);
        }
        this.gvUserId = userVO.getGvUserId();
        this.gvUserNm = userVO.getGvUserNm();
        this.gvMailAddr = userVO.getGvMailAddr();
        this.gvTelNo = userVO.getGvTelNo();
        this.gvDeptCd = userVO.getGvDeptCd();
        this.gvPosiCd = userVO.getGvPosiCd();
        this.gvDutyCd = userVO.getGvDutyCd();
        this.gvMgrUserId = userVO.getGvMgrUserId();
        this.gvLangCd = userVO.getLangCd();
    }

    @PreDestroy
    public void destroy() {
        clearUserVO();
    }

    private void clearUserVO() {
        this.gvUserId = null;
        this.gvUserNm = null;
        this.gvMailAddr = null;
        this.gvTelNo = null;
        this.gvDeptCd = null;
        this.gvPosiCd = null;
        this.gvDutyCd = null;
        this.gvMgrUserId = null;
        this.gvLangCd = null;
    }
}

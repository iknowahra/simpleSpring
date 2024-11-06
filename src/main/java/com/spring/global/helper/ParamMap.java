package com.spring.global.helper;

import com.google.common.base.CaseFormat;
import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.utils.SecurityUtil;

import java.util.HashMap;

public class ParamMap extends HashMap {
    private static final long serialVersionUID = 1L;

    @Override
    public Object put(Object key, Object value) {
        return super.put(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, (String) key), value);
    }

    public ParamMap() {
        CurrentUserVO userVO = SecurityUtil.getUserVO();
        if(userVO == null) throw new CustomAuthenticationException(ErrorCode.INVALID_USER_DATA);
        putAllFromUserVO(userVO);
    }

    private void putAllFromUserVO(CurrentUserVO currentUserVO) {
        this.put("GV_USER_ID", currentUserVO.getGvUserId());
        this.put("GV_USER_NM", currentUserVO.getGvUserNm());
        this.put("GV_MAIL_ADDR", currentUserVO.getGvMailAddr());
        this.put("GV_TEL_NO", currentUserVO.getGvTelNo());
        this.put("GV_DEPT_CD", currentUserVO.getGvDeptCd());
        this.put("GV_POSI_CD", currentUserVO.getGvPosiCd());
        this.put("GV_DUTY_CD", currentUserVO.getGvDutyCd());
        this.put("GV_LANG_CD", currentUserVO.getGvLangCd());
        this.put("GV_MGR_USER_ID", currentUserVO.getGvMgrUserId());
    }

}

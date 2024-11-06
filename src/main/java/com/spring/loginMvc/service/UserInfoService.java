package com.spring.loginMvc.service;

import com.spring.global.helper.ParamMap;

import java.util.List;
import java.util.Map;

public interface UserInfoService {
    List<ParamMap> getMenuListByUser (Map<String, Object> paramMap);

    List<ParamMap> getCodeList(Map<String, Object> paramMap);

    List<ParamMap> getCode(Map<String, Object> paramDTO);

    List<ParamMap> getUserFavMenuList(Map<String, Object> paramMap);

    List<ParamMap> getNotiList(Map<String, Object> paramMap);

    List<ParamMap> getLangCode();

    List<ParamMap> getMessageByLangCode(String langCd);

    }

package com.spring.loginMvc.service.impl;

import com.spring.global.helper.ParamMap;
import com.spring.global.helper.LoggingBuilder;
import com.spring.loginMvc.dao.UserInfoMapper;
import com.spring.loginMvc.service.UserInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserInfoServiceImpl implements UserInfoService {
    
    private final UserInfoMapper mapper;
    private final LoggingBuilder loggingBuilder;
    
    @Override
    public List<ParamMap> getMenuListByUser(Map<String, Object> paramMap) {
        return mapper.selectMenuListByUser(paramMap);
    }

    @Override
    public List<ParamMap> getCodeList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<ParamMap> getCode(Map<String, Object> paramDTO) {
        return null;
    }

    @Override
    public List<ParamMap> getUserFavMenuList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<ParamMap> getNotiList(Map<String, Object> paramMap) {
        return null;
    }

    @Override
    public List<ParamMap> getLangCode() {
        return null;
    }

    @Override
    public List<ParamMap> getMessageByLangCode(String langCd) {
        return null;
    }
}

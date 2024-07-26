package com.spring.loginMvc.dao;

import com.spring.loginMvc.dto.PwChangeDTO;
import com.spring.loginMvc.dto.UserDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LoginMapper {
//    Map<String, Object> selectUser(String userId);
    UserDTO selectUser(String userId);

    void updateLgnFailCntByPlusOne(String userId);

    void updateLockYNtoY(String userId);

    void updateLogFailCntToZero(String userId);

    void updateUserPassword(PwChangeDTO dto);

}

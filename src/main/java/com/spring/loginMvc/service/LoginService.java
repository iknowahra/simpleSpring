package com.spring.loginMvc.service;

import com.spring.global.security.CustomUserDetails;
import com.spring.loginMvc.dto.PwChangeDTO;
import com.spring.loginMvc.dto.UserDTO;

public interface LoginService {
    void changePassword(PwChangeDTO pwChangeDTO);

    void checkIfUserLocked(CustomUserDetails user);

    void updateFailCnt(CustomUserDetails user, PwChangeDTO pwChangeDTO);

    void withNewTxUpdatePassword(PwChangeDTO pwChangeDTO, UserDTO user);

    void updateLgnFailCntByPlusOne(String userId);

    void updateLockYNtoY(String userId);

    void updateLogFailCntToZero(String userId);
}

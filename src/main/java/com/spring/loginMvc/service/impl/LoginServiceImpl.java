package com.spring.loginMvc.service.impl;

import com.spring.global.error.ErrorCode;
import com.spring.global.error.Exception.CustomAuthenticationException;
import com.spring.global.error.Exception.CustomIllegalArgumentException;
import com.spring.global.helper.AppConst;
import com.spring.global.helper.CurrentUserVO;
import com.spring.global.security.CustomUserDetails;
import com.spring.loginMvc.dao.LoginMapper;
import com.spring.loginMvc.dto.PwChangeDTO;
import com.spring.loginMvc.dto.UserDTO;
import com.spring.loginMvc.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {
    private final LoginMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    @Override
    public void changePassword(PwChangeDTO pwChangeDTO) {
        String userId = pwChangeDTO.getUserId();
        CustomUserDetails user = (CustomUserDetails) userDetailsService.loadUserByUsername(userId);

        // 아직 토큰 발행 전이라 유저 정보를 세팅해줘야한다.
        CurrentUserVO currentUser = new CurrentUserVO(user.getUserDTO(), user.getLangCd());
        pwChangeDTO.setUserVO(currentUser);


        // 잠긴 사용자 여부 체크
        checkIfUserLocked(user);
        // 잘못된 현재 비밀번호 제공시 실패 횟수 증가
        updateFailCnt(user, pwChangeDTO);

        // 비밀번호 일치할 경우
        updateLogFailCntToZero(userId); // 비밀번호 실패 횟수 0으로 초기화

        // 새로운 비번으로 패스워드 변경
        withNewTxUpdatePassword(pwChangeDTO, user.getUserDTO());
//        throw new RuntimeException("hihi");
    }

    /**
     * 사용자 잠금 상태를 확인하는 로직
     *
     * @param user
     */
    @Override
    public void checkIfUserLocked(CustomUserDetails user) {
        // 계정 미사용 기간 45일 초과 확인
        if (user.isAccountNotUsed()) {
            this.updateLockYNtoY(user.getUsername()); // 계정 잠금
            throw new CustomAuthenticationException(ErrorCode.LOCKED_USER);
        }
        // 계정 잠금 여부 확인
        if (!user.isAccountNonLocked()) {
            if (user.isAccountNotUsed()) { // 계정 미사용 기간 45일 초과일 경우
                throw new CustomAuthenticationException(ErrorCode.INACTIVE_USER);
            } else { // 비밀번호 연속 오류일 경우
                throw new CustomAuthenticationException(ErrorCode.LOCKED_BY_WRONG_PASSWORD, user.getLgnFailCnt());
            }
        }
    }

    @Override
    public void updateFailCnt(CustomUserDetails user, PwChangeDTO pwChangeDTO) {
        // 비밀번호 변경할 때, 잘못된 현재 비밀번호를 제공할 경우, 실패 횟수 증가
        if (!passwordEncoder.matches(pwChangeDTO.getCurrentPw(), user.getPassword())) {
            if (user.getLgnFailCnt() < AppConst.LOGIN_FAIL_COUNT_LIMIT) {// 비밀번호가 일치하지 않을 경우
                this.updateLgnFailCntByPlusOne(user.getUsername()); // 비밀번호 실패 횟수 1 증가
                throw new CustomIllegalArgumentException(ErrorCode.PASSWORD_N_TIMES_MISMATCH, user.getLgnFailCnt() + 1);
            } else {
                this.updateLockYNtoY(user.getUsername()); // 계정 잠금
                throw new CustomAuthenticationException(ErrorCode.LOCKED_BY_WRONG_PASSWORD, user.getLgnFailCnt());
            }
        }
    }

    @Override
    public void withNewTxUpdatePassword(PwChangeDTO pwChangeDTO, UserDTO user) {
        if (!pwChangeDTO.isPwNewAndPwConfirmMatches()) {
            throw new CustomIllegalArgumentException(ErrorCode.PASSWORD_MISMATCH); //"새비밀번호와 비밀번호확인이 일치하지 않습니다."
        }

        if (checkPwUsedBefore(pwChangeDTO, user)) {
            throw new CustomIllegalArgumentException(ErrorCode.PASSWORD_ALREADY_USED); //"기존에 사용되었던 비밀번호 입니다."
        }
        // 새로운 비밀번호 암호화 후 db 저장
        pwChangeDTO.setNewPw(passwordEncoder.encode(pwChangeDTO.getNewPw()));
        mapper.updateUserPassword(pwChangeDTO);
    }

    /**
     * 과거에 사용했던 비밀번호랑 바꾸려는 비밀번호가 일치하는 지 체크
     *
     * @param pwChangeDTO
     * @param user
     * @return
     */
    private boolean checkPwUsedBefore(PwChangeDTO pwChangeDTO, UserDTO user) {
        return passwordEncoder.matches(pwChangeDTO.getNewPw(), user.getUserPw())
                || passwordEncoder.matches(pwChangeDTO.getNewPw(), user.getPw1Chg())
                || passwordEncoder.matches(pwChangeDTO.getNewPw(), user.getPw2Chg())
                || passwordEncoder.matches(pwChangeDTO.getNewPw(), user.getPw3Chg())
                || passwordEncoder.matches(pwChangeDTO.getNewPw(), user.getPw4Chg())
                || passwordEncoder.matches(pwChangeDTO.getNewPw(), user.getPw5Chg())
                ;
    }

    @Override
    public void updateLgnFailCntByPlusOne(String userId) {
        mapper.updateLgnFailCntByPlusOne(userId);
    }

    @Override
    public void updateLockYNtoY(String userId) {
        mapper.updateLockYNtoY(userId);
    }

    @Override
    public void updateLogFailCntToZero(String userId) {
        mapper.updateLogFailCntToZero(userId);
    }
}

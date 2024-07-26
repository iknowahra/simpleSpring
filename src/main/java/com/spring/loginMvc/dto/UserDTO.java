package com.spring.loginMvc.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.ibatis.type.Alias;


@Data
@NoArgsConstructor
@Alias("UserDTO")
public class UserDTO {
    private String userId; /* 사용자아이디 */
    private String userPw; /* 사용자비밀번호 */
    private String userCls; /* 사용자구분 */
    private String userNm; /* 사용자명 */
    private String userEngNm; /* 사용자 영문명 */
    private String mailAddr; /* 이메일주소 */
    private String telNo; /* 전화번호 */
    private String deptCd; /* 부서코드 */
    private String deptNm; /* 부서명 */
    private String dutyCd; /* 직책코드 */
    private String posiCd; /* 직위코드 */
    private String posiNm; /* 직위명 */
    private String retiYn; /* 퇴직여부 */
    private String remk; /* 비고 */
    private String pwChgDtm; /* 비밀번호 변경일자 */
    private int lgnFailCnt; /* 로그인실패횟수 */
    private String lastLgnDtm; /* 최종로그인일자 */
    private String lockYn; /* 잠김여부 */
    private String frstLgnYn; /* 최초로그인여부 */

    private String pw1Chg; /* 비밀번호1변경 */
    private String pw1ChgDtm; /* 비밀번호1변경일시 */
    private String pw2Chg; /* 비밀번호2변경 */
    private String pw2ChgDtm; /* 비밀번호2변경일시 */
    private String pw3Chg; /* 비밀번호3변경 */
    private String pw3ChgDtm; /* 비밀번호3변경일시 */
    private String pw4Chg; /* 비밀번호4변경 */
    private String pw4ChgDtm; /* 비밀번호4변경일시 */
    private String pw5Chg; /* 비밀번호5변경 */
    private String pw5ChgDtm; /* 비밀번호5변경일시 */

    private String regId; /* 등록자아이디 */
    private String regDtm; /* 등록일시 */
    private String updId; /* 수정자아이디 */
    private String updDtm; /* 수정일시 */
    private String mgrUserId; /* 부서장아이디 */

    public UserDTO(final String userId) {
        if (((userId == null) || "".equals(userId))) {
            throw new IllegalArgumentException("Cannot pass null or empty values to constructor");
        }
        this.userId = userId;
    }

    public UserDTO(final String userId, final String userPw) {
        this(userId);
        this.userPw = userPw;
    }
}

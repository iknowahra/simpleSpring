-- 테이블 삭제
DROP TABLE IF EXISTS ZZ_USER_M CASCADE;

-- 테이블 생성
CREATE TABLE ZZ_USER_M (
                           USER_ID VARCHAR(20) NOT NULL,
                           USER_CLS CHAR(1),
                           USER_PW VARCHAR(300),
                           USER_NM VARCHAR(300),
                           USER_ENG_NM VARCHAR(300),
                           MAIL_ADDR VARCHAR(300),
                           TEL_NO VARCHAR(300),
                           DEPT_CD VARCHAR(20),
                           DUTY_CD VARCHAR(4),
                           DUTY_NM VARCHAR(100),
                           POSI_CD VARCHAR(4),
                           POSI_NM VARCHAR(100),
                           RETI_YN CHAR(1) DEFAULT 'N',
                           REMK VARCHAR(2000),
                           PW_CHG_DTM TIMESTAMP,
                           LGN_FAIL_CNT INTEGER DEFAULT 0,
                           LAST_LGN_DTM TIMESTAMP,
                           LOCK_YN CHAR(1) DEFAULT 'N',
                           FRST_LGN_YN CHAR(1),
                           PW_1CHG VARCHAR(200),
                           PW_1CHG_DTM DATE,
                           PW_2CHG VARCHAR(200),
                           PW_2CHG_DTM DATE,
                           PW_3CHG VARCHAR(200),
                           PW_3CHG_DTM DATE,
                           PW_4CHG VARCHAR(200),
                           PW_4CHG_DTM DATE,
                           PW_5CHG VARCHAR(200),
                           PW_5CHG_DTM DATE,
                           REG_ID VARCHAR(20) NOT NULL,
                           REG_DTM TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           UPD_ID VARCHAR(20) NOT NULL,
                           UPD_DTM TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT PK_ZZ_USER_M PRIMARY KEY (USER_ID)
);

-- 주석 추가
COMMENT ON TABLE ZZ_USER_M IS '사용자마스터';

COMMENT ON COLUMN ZZ_USER_M.USER_ID IS '사용자아이디';
COMMENT ON COLUMN ZZ_USER_M.USER_CLS IS '사용자구분';
COMMENT ON COLUMN ZZ_USER_M.USER_PW IS '사용자비밀번호';
COMMENT ON COLUMN ZZ_USER_M.USER_NM IS '사용자명';
COMMENT ON COLUMN ZZ_USER_M.USER_ENG_NM IS '사용자 영문명';
COMMENT ON COLUMN ZZ_USER_M.MAIL_ADDR IS '이메일주소';
COMMENT ON COLUMN ZZ_USER_M.TEL_NO IS '전화번호';
COMMENT ON COLUMN ZZ_USER_M.DEPT_CD IS '부서코드';
COMMENT ON COLUMN ZZ_USER_M.DUTY_CD IS '직책코드';
COMMENT ON COLUMN ZZ_USER_M.DUTY_NM IS '직책명';
COMMENT ON COLUMN ZZ_USER_M.POSI_CD IS '직위코드';
COMMENT ON COLUMN ZZ_USER_M.POSI_NM IS '직위명';
COMMENT ON COLUMN ZZ_USER_M.RETI_YN IS '퇴직여부';
COMMENT ON COLUMN ZZ_USER_M.REMK IS '비고';
COMMENT ON COLUMN ZZ_USER_M.PW_CHG_DTM IS '비밀번호 변경일자';
COMMENT ON COLUMN ZZ_USER_M.LGN_FAIL_CNT IS '로그인실패횟수';
COMMENT ON COLUMN ZZ_USER_M.LAST_LGN_DTM IS '최종로그인일자';
COMMENT ON COLUMN ZZ_USER_M.LOCK_YN IS '잠김여부';
COMMENT ON COLUMN ZZ_USER_M.FRST_LGN_YN IS '최초로그인여부';
COMMENT ON COLUMN ZZ_USER_M.PW_1CHG IS '비밀번호1변경';
COMMENT ON COLUMN ZZ_USER_M.PW_1CHG_DTM IS '비밀번호1변경일시';
COMMENT ON COLUMN ZZ_USER_M.PW_2CHG IS '비밀번호2변경';
COMMENT ON COLUMN ZZ_USER_M.PW_2CHG_DTM IS '비밀번호2변경일시';
COMMENT ON COLUMN ZZ_USER_M.PW_3CHG IS '비밀번호3변경';
COMMENT ON COLUMN ZZ_USER_M.PW_3CHG_DTM IS '비밀번호3변경일시';
COMMENT ON COLUMN ZZ_USER_M.PW_4CHG IS '비밀번호4변경';
COMMENT ON COLUMN ZZ_USER_M.PW_4CHG_DTM IS '비밀번호4변경일시';
COMMENT ON COLUMN ZZ_USER_M.PW_5CHG IS '비밀번호5변경';
COMMENT ON COLUMN ZZ_USER_M.PW_5CHG_DTM IS '비밀번호5변경일시';
COMMENT ON COLUMN ZZ_USER_M.REG_ID IS '등록자아이디';
COMMENT ON COLUMN ZZ_USER_M.REG_DTM IS '등록일시';
COMMENT ON COLUMN ZZ_USER_M.UPD_ID IS '수정자아이디';
COMMENT ON COLUMN ZZ_USER_M.UPD_DTM IS '수정일시';
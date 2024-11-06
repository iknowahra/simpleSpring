package com.spring.global.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 발생한 오류에 대한 코드와 메시지 ENUM
 */
@Getter
public enum ErrorCode {
    // 인증 && 인가 A
    AUTHENTICATION_EXCEPTION(HttpStatus.UNAUTHORIZED, "A-001", "사용자 인증 중 알 수 없는 에러가 발생 하였습니다."),
    NOT_EXISTS_AUTHORIZATION(HttpStatus.UNAUTHORIZED, "A-002", "Authorization Header가 빈 값입니다."),
    NOT_VALID_BEARER_GRANT_TYPE(HttpStatus.UNAUTHORIZED, "A-003", "인증 타입이 Bearer 타입이 아닙니다."),
    FORBIDDEN_ROLE(HttpStatus.FORBIDDEN, "A-010", "해당 Role이 아닙니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "A-011", "접근할 수 없는 사용자 입니다."),
    PASSWORD_CHANGE_REQUIRED(HttpStatus.UNAUTHORIZED, "A-012", "임시 비밀번호입니다. 비밀번호를 변경하세요."),
    TEMPORARY_PASSWORD_CHANGE_REQUIRED(HttpStatus.UNAUTHORIZED, "A-013", "임시 비밀번호입니다. 비밀번호를 변경하세요."),
    EXPIRED_PASSWORD_CHANGE_REQUIRED(HttpStatus.UNAUTHORIZED, "A-014", "만료된 비밀번호입니다. 비밀번호를 변경하세요."),
    USERID_OR_PASSWORD_NOT_MATCH(HttpStatus.UNAUTHORIZED, "A-015", "아이디 또는 비밀번호 정보가 일치하지 않습니다."),
    PASSWORD_N_TIMES_MISMATCH(HttpStatus.UNAUTHORIZED, "A-016", "비밀번호가 {0}회 이상 일치하지 않았습니다"),
    PASSWORD_MISMATCH(HttpStatus.UNAUTHORIZED, "A-017", "비밀번호가 일치하지 않습니다"),
    PASSWORD_CONFIRM_NOT_MATCH(HttpStatus.BAD_REQUEST, "A-018", "새비밀번호와 비밀번호확인이 일치하지 않습니다."),
    PASSWORD_ALREADY_USED(HttpStatus.BAD_REQUEST, "A-019", "이전에 사용되었던 비밀번호 입니다"),

    // 인증 && 인가 A - 토큰 관련
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-100", "{0} 토큰이 만료되었습니다."),
    TOKEN_PROBLEM(HttpStatus.UNAUTHORIZED, "A-101", "토큰에 이상이 있습니다. {0}"),
    TOKEN_SIGN_NOT_MATCH(HttpStatus.UNAUTHORIZED, "A-103", "토큰 서명이 일치하지 않습니다."),
    NOT_VALID_TOKEN(HttpStatus.UNAUTHORIZED, "A-104", "해당 토큰은 유효한 토큰이 아닙니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-105", "해당 refresh token은 존재하지 않습니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "A-106", "해당 refresh token은 만료됐습니다."),
    NOT_ACCESS_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "A-107", "해당 토큰은 ACCESS TOKEN이 아닙니다."),
    TOKEN_UNSUPPORTED(HttpStatus.UNAUTHORIZED, "A-108", "지원하지 않는 토큰입니다. {0}"),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "A-109", "access token을 찾을 수 없습니다."),

    // 사용자 관련 U,
    NOT_EXISTS_USER(HttpStatus.UNAUTHORIZED, "U-001", "존재하지 않는 사용자입니다."),
    NOT_EXISTS_USER_ID(HttpStatus.UNAUTHORIZED, "U-002", "존재하지 않는 유저 아이디입니다."),
    NOT_EXISTS_USERNAME(HttpStatus.UNAUTHORIZED, "U-003", "존재하지 않는 유저 닉네임입니다."),
    NOT_EXISTS_USER_EMAIL(HttpStatus.UNAUTHORIZED, "U-004", "존재하지 않는 유저 이메일입니다."),
    ALREADY_REGISTERED_USER_ID(HttpStatus.UNAUTHORIZED, "U-005", "이미 존재하는 유저 아이디입니다."),
    NOT_EXISTS_USER_PASSWORD(HttpStatus.UNAUTHORIZED, "U-006", "존재하지 않는 유저 비밀번호입니다."),
    INVALID_USER_DATA(HttpStatus.UNAUTHORIZED, "U-007", "잘못된 유저 정보입니다."),
    INVALID_ADMIN(HttpStatus.UNAUTHORIZED, "U-008", "관리자가 아닙니다."),
    LOCKED_BY_WRONG_PASSWORD(HttpStatus.UNAUTHORIZED, "U-009", "%n회 이상 오류가 발생하여 잠금처리 되었습니다. 관리자에게 문의하세요."),
    INACTIVE_USER(HttpStatus.UNAUTHORIZED, "U-010", "장기 미접속 사용자로 계정이 잠겨 있습니다. 관리자에게 문의하세요."),
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "U-011", "사용자 정보를 찾을 수 없습니다."),
    LOCKED_USER(HttpStatus.UNAUTHORIZED, "U-012", "계정이 잠금처리 되었습니다. 관리자에게 문의하세요."),

    // 일반 입력값 관련 C
    BUSINESS_EXCEPTION_ERROR(HttpStatus.NOT_FOUND, "C-001", "요청이 잘못되었습니다."), // 분류하기 힘든 사용자의 비즈니스 에러 통칭
    NOT_VALID_ERROR(HttpStatus.NOT_FOUND, "C-002", "전달받은 값이 유효하지 않습니다."), // @RequestBody 및 @RequestParam,
    // @PathVariable
    // 값이 유효하지 않음
    DB_CANNOT_SAVE(HttpStatus.INTERNAL_SERVER_ERROR, "C-003", "데이터가 저장이 이뤄지지 않습니다."), // @RequestBody 및
    // @RequestParam,
    // @PathVariable 값이 유효하지 않음

    // 서버 관련 G
    NOT_FOUND(HttpStatus.NOT_FOUND, "G-001", "찾을 수 없는 페이지 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "G-002", "알 수 없는 내부오류가 발생했습니다. 관리자에게 문의하세요."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "G-003", "잘못된 요청입니다"),

    // DB 관련 D
    DB_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "D-001", "CannotSerializeTransactionException"),
    DB_INVALID_RESULT_SET_ACCESS(HttpStatus.INTERNAL_SERVER_ERROR, "D-002",
            "InvalidResultSetAccessException : 쿼리 결과가 없는데 메서드로 데이터를 가져오라고 할 경우, 잘못된 컬럼 이름이나 인덱스를 사용했을 때"),
    DB_DUPLICATE_KEY(HttpStatus.INTERNAL_SERVER_ERROR, "D-003",
            "DuplicateKeyException : 고유성 제한 위반과 같은 데이터 삽입 또는 업데이트시 무결성 위반"),
    DB_DATA_INTEGRITY_VIOLATION(HttpStatus.INTERNAL_SERVER_ERROR, "D-004",
            "DataIntegrityViolationException : 등록된 데이터가 컬럼의 속성과 다릅니다. (길이, 속성, 필수입력항목 등..)"),
    DB_DATA_ACCESS_RESOURCE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "D-005",
            "DataAccessResourceFailureException :  데이터 액세스 리소스가 완전히 실패했습니다 (예 : 데이터베이스에 연결할 수 없음)"),
    DB_DEADLOCK_LOSER_DATA_ACCESS(HttpStatus.INTERNAL_SERVER_ERROR, "D-006",
            "DeadlockLoserDataAccessException : 교착 상태로 인한 현재 작업 실패"),
    DB_CANNOT_ACQUIRE_LOCK_EXCEPTION(HttpStatus.INTERNAL_SERVER_ERROR, "D-007",
            "CannotAcquireLockException : 트랜잭션 중에 락을 얻을 수 없을 떼"),
    DB_CANNOT_SERIALIZE_TRANSACTION(HttpStatus.INTERNAL_SERVER_ERROR, "D-008",
            "CannotSerializeTransactionException : 직렬화 모드에서 트랜잭션을 완료 할 수 없음"),
    DB_BAD_SQL_GRAMMAR(HttpStatus.INTERNAL_SERVER_ERROR, "D-009", "BadSqlGrammarException"),

    // Program 관련 P
    NOT_AVAILABLE_CONSTRUCTOR(HttpStatus.INTERNAL_SERVER_ERROR, "P-001", "생성자를 사용할 수 없습니다.");
    private final HttpStatus httpStatus;
    private final String errorCode;
    private final String message;
    private final String headerMessage;

    ErrorCode(HttpStatus httpStatus, String errorCode, String message) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.message = message;
        this.headerMessage = generateHeaderMessage(this.name());
    }

    /**
     * Enum 이름 자체를 소문자로 바꿔줌
     * USER_NOT_FOUND -> user_not_found
     *
     * @param enumName
     * @return
     */
    private String generateHeaderMessage(String enumName) {
        return enumName
                .replaceAll("([a-z])([A-Z]+)", "$1_$2") // 카멜 케이스를 밑줄로 변환
                .replace("-", "_") // 하이픈을 밑줄로 변환
                .toLowerCase(); // 소문자로 변환
    }
}
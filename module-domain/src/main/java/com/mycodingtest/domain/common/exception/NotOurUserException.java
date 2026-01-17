package com.mycodingtest.domain.common.exception;

/**
 * <h3>미등록 사용자 예외 (NotOurUserException)</h3>
 * <p>
 * 시스템에 등록되지 않은 사용자가 인증을 시도하거나,
 * 존재하지 않는 사용자 정보를 조회할 때 발생하는 예외입니다.
 * </p>
 * <p>
 * 주로 OAuth 로그인 후 사용자 정보 동기화 과정에서 사용됩니다.
 * </p>
 */
public class NotOurUserException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Not our user.";

    /**
     * 기본 메시지를 사용하여 예외를 생성합니다.
     */
    public NotOurUserException() {
        super(DEFAULT_MESSAGE);
    }
}

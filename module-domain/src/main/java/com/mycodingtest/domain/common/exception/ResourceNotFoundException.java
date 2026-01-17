package com.mycodingtest.domain.common.exception;

/**
 * <h3>리소스 미발견 예외 (ResourceNotFoundException)</h3>
 * <p>
 * 요청한 리소스가 시스템에 존재하지 않을 때 발생하는 예외입니다.
 * 리뷰, 문제, 채점 결과 등 다양한 도메인 객체 조회 시 사용됩니다.
 * </p>
 * <p>
 * HTTP 응답 시 {@code 404 Not Found} 상태 코드로 변환됩니다.
 * </p>
 */
public class ResourceNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Not found your resource";

    /**
     * 기본 메시지를 사용하여 예외를 생성합니다.
     */
    public ResourceNotFoundException() {
        super(DEFAULT_MESSAGE);
    }
}

package com.mycodingtest.domain.common.exception;

/**
 * <h3>소유권 검증 실패 예외 (InvalidOwnershipException)</h3>
 * <p>
 * 사용자가 본인 소유가 아닌 리소스에 접근하려 할 때 발생하는 예외입니다.
 * 주로 리뷰, 채점 결과 등 개인 데이터에 대한 무단 접근 시도 시 발생합니다.
 * </p>
 * <p>
 * HTTP 응답 시 {@code 403 Forbidden} 상태 코드로 변환됩니다.
 * </p>
 *
 * @see com.mycodingtest.domain.review.Review#validateOwnership(Long)
 */
public class InvalidOwnershipException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Access is forbidden for this resource. Because not your resource.";

    /**
     * 기본 메시지를 사용하여 예외를 생성합니다.
     */
    public InvalidOwnershipException() {
        super(DEFAULT_MESSAGE);
    }
}

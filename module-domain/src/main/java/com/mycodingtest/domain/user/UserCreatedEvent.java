package com.mycodingtest.domain.user;

/**
 * <h3>사용자 생성 이벤트 (UserCreatedEvent)</h3>
 * <p>
 * 신규 사용자가 시스템에 등록되었을 때 발행되는 도메인 이벤트입니다.
 * 이 이벤트를 구독하는 리스너에서 환영 문제 생성 등의 후속 처리를 수행합니다.
 * </p>
 *
 * @param userId 생성된 사용자의 ID
 */
public record UserCreatedEvent(Long userId) {
}

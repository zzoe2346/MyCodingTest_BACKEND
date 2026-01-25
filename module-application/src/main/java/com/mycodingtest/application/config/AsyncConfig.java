package com.mycodingtest.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <h3>비동기 처리 설정 (AsyncConfig)</h3>
 * <p>
 * Spring의 비동기 처리 기능을 활성화합니다.
 * {@code @Async} 어노테이션이 붙은 메서드가 별도의 스레드에서 실행됩니다.
 * </p>
 */
@Configuration
@EnableAsync
public class AsyncConfig {
}

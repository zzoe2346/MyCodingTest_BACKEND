package com.mycodingtest.application.user.event;

import com.mycodingtest.application.user.command.WelcomeService;
import com.mycodingtest.domain.user.UserCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * <h3>사용자 생성 이벤트 리스너 (UserCreatedEventListener)</h3>
 * <p>
 * 신규 사용자 생성 이벤트를 비동기로 처리합니다.
 * 환영 문제 생성 등의 후속 작업을 수행합니다.
 * </p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreatedEventListener {

    private final WelcomeService welcomeService;

    /**
     * 사용자 생성 이벤트를 비동기로 처리합니다.
     * <p>
     * 신규 사용자에게 환영 문제를 생성하여 서비스 온보딩을 돕습니다.
     * </p>
     *
     * @param event 사용자 생성 이벤트
     */
    @Async
    @EventListener
    public void handleUserCreated(UserCreatedEvent event) {
        try {
            welcomeService.createWelcomeProblemForUser(event.userId());
        } catch (Exception e) {
            log.error("Failed to create welcome problem for user: {}", event.userId(), e);
        }
    }
}

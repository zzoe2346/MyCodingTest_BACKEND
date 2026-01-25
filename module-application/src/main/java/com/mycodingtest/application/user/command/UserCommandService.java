package com.mycodingtest.application.user.command;

import com.mycodingtest.domain.user.User;
import com.mycodingtest.domain.user.UserCreatedEvent;
import com.mycodingtest.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * <h3>사용자 관리 서비스 (UserService)</h3>
 * <p>
 * 회원 가입, 정보 조회 등 사용자 엔티티와 관련된 기본 유스케이스를 담당합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserCommandService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 신규 사용자를 시스템에 등록하거나 기존 사용자를 조회합니다.
     * <p>
     * 신규 사용자인 경우, {@link UserCreatedEvent}를 발행하여
     * 환영 문제 생성 등의 후속 처리를 비동기로 수행합니다.
     * </p>
     */
    @Transactional
    public User syncUser(SyncUserCommand command) {
        Optional<User> existingUser = userRepository.findUser(command.provider(), command.oauthId());
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        User newUser = userRepository.save(
                User.from(command.name(), command.email(), command.picture(), command.provider(), command.oauthId()));
        eventPublisher.publishEvent(new UserCreatedEvent(newUser.getId()));
        return newUser;
    }

}
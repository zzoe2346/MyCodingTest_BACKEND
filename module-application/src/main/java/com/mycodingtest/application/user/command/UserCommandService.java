package com.mycodingtest.application.user.command;

import com.mycodingtest.domain.user.User;
import com.mycodingtest.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * 신규 사용자를 시스템에 등록합니다.
     */
    @Transactional
    public User syncUser(SyncUserCommand command) {
        return userRepository.findUser(command.provider(), command.oauthId())
                .orElseGet(() -> userRepository.save(User.from(command.name(), command.email(), command.picture(), command.provider(), command.oauthId())));
    }

}
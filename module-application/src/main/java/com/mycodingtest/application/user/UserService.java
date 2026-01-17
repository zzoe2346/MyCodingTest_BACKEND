package com.mycodingtest.application.user;

import com.mycodingtest.domain.user.User;
import com.mycodingtest.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * <h3>사용자 관리 서비스 (UserService)</h3>
 * <p>
 * 회원 가입, 정보 조회 등 사용자 엔티티와 관련된 기본 유스케이스를 담당합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    /**
     * 소셜 정보로 기존 가입 여부를 확인합니다.
     */
    public Optional<User> findUser(String provider, String oauthId) {
        return userRepository.findUser(provider, oauthId);
    }

    /**
     * 신규 사용자를 시스템에 등록합니다.
     */
    public User getOrCreateUser(String name, String email, String picture, String provider, String oauthId) {
        return userRepository.save(
                User.builder()
                        .name(name)
                        .email(email)
                        .picture(picture)
                        .oauthProvider(provider)
                        .oauthId(oauthId)
                        .build()
        );
    }

    /**
     * 사용자의 상세 프로필 정보를 조회합니다.
     */
//    public UserDetailInfoResponse getUserDetailInfo(Long userId) {
////        User user = userRepository.findById(userId)
////                .orElseThrow(ResourceNotFoundException::new);
//        return null;
//    }

}
package com.mycodingtest.domain.user;


import java.util.Optional;

/**
 * <h3>User Repository Interface</h3>
 * <p>
 * 사용자 정보에 대한 영속성을 관리합니다.
 * 소셜 로그인(OAuth2) 과정에서 사용자를 식별하기 위한 조회 메서드가 핵심입니다.
 * </p>
 */
public interface UserRepository {

    /**
     * 소셜 제공자(Provider)와 제공자 측 식별자(OauthId)로 사용자를 조회합니다.
     * <p>로그인 시도 시, 기존 회원인지 신규 회원인지 판별하는 기준으로 사용됩니다.</p>
     *
     * @param oauthProvider 소셜 서비스 이름 (google, kakao 등)
     * @param oauthId       소셜 서비스 내에서의 유저 고유 ID
     * @return 사용자 객체 (Optional)
     */
    Optional<User> findUser(String oauthProvider, String oauthId);

    User save(User user);

}
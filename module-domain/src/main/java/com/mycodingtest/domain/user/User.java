package com.mycodingtest.domain.user;

import lombok.Builder;
import lombok.Getter;

/**
 * <h3>사용자 (User)</h3>
 * <p>
 * 시스템의 사용자를 나타내는 도메인 엔티티입니다.
 * OAuth 소셜 로그인을 통해 가입하며, 여러 개의 복습
 * 기록({@link com.mycodingtest.domain.review.Review})과
 * 채점 결과({@link com.mycodingtest.domain.judgment.Judgment})를 가질 수 있습니다.
 * </p>
 * <p>
 * <b>사용자 식별:</b> {@code oauthProvider}와 {@code oauthId}의 조합으로 유일하게 식별됩니다.
 * </p>
 *
 * @see UserRepository
 */
@Builder
@Getter
public class User {

    /**
     * 시스템 내부 고유 식별자
     */
    private Long id;

    /**
     * 사용자 이름 (소셜 계정에서 가져온 이름)
     */
    private String name;

    /**
     * 소셜 계정 이메일
     */
    private String email;

    /**
     * 프로필 이미지 URL
     */
    private String picture;

    /**
     * OAuth 소셜 로그인 제공자
     * <p>
     * 예: "google", "kakao"
     * </p>
     */
    private String oauthProvider;

    /**
     * 소셜 공급자별 고유 식별자
     * <p>
     * 제공자 내에서 사용자를 유일하게 구분하는 ID입니다.
     * </p>
     */
    private String oauthId;
}

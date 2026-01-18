package com.mycodingtest.domain.user;

import lombok.AccessLevel;
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
@Builder(access = AccessLevel.PRIVATE)
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

    /**
     * OAuth 로그인을 통해 새로운 사용자를 생성합니다.
     *
     * @param name          사용자 이름
     * @param email         이메일
     * @param picture       프로필 이미지 URL
     * @param oauthProvider OAuth 제공자 (예: "google", "kakao")
     * @param oauthId       제공자별 고유 ID
     * @return 생성된 User 인스턴스
     * @throws IllegalArgumentException 필수 값이 누락된 경우
     */
    public static User from(String name, String email, String picture, String oauthProvider, String oauthId) {
        if (oauthProvider == null || oauthProvider.isBlank()) {
            throw new IllegalArgumentException("OAuth 제공자는 필수입니다");
        }
        if (oauthId == null || oauthId.isBlank()) {
            throw new IllegalArgumentException("OAuth ID는 필수입니다");
        }
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .oauthProvider(oauthProvider)
                .oauthId(oauthId)
                .build();
    }

    /**
     * 영속성 계층에서 조회된 데이터로 User를 복원합니다.
     * <p>
     * 인프라 계층의 Mapper에서 사용됩니다.
     * </p>
     *
     * @param id            시스템 내부 ID
     * @param name          사용자 이름
     * @param email         이메일
     * @param picture       프로필 이미지 URL
     * @param oauthProvider OAuth 제공자
     * @param oauthId       제공자별 고유 ID
     * @return 복원된 User 인스턴스
     */
    public static User from(Long id, String name, String email, String picture, String oauthProvider, String oauthId) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .picture(picture)
                .oauthProvider(oauthProvider)
                .oauthId(oauthId)
                .build();
    }
}

package com.mycodingtest.domain.user;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class User {

    private Long id;
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
     * OAuth 소셜 로그인 제공자 (예: google, kakao)
     */
    private String oauthProvider;

    /**
     * <b>소셜 공급자별 고유 식별자</b>
     * <p>제공자 내에서 사용자를 유일하게 구분하는 ID입니다.</p>
     */
    private String oauthId;
}

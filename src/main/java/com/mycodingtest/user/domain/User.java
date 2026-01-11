package com.mycodingtest.user.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * <h3>사용자 (User)</h3>
 * <p>
 * 서비스를 이용하는 사용자의 인증 및 프로필 정보를 관리합니다.
 * 본 시스템은 OAuth2 기반 소셜 로그인을 제공하며, 외부 공급자(Google, Kakao 등)로부터 전달받은 정보를 기반으로 식별합니다.
 * </p>
 */
@Entity
@Getter
@NoArgsConstructor
@Table(name = "`USER`")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 사용자의 실명 또는 닉네임
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
     * OAuth 소셜 로그인 제공자 (예: google, kakao)
     */
    private String oauthProvider;

    /**
     * <b>소셜 공급자별 고유 식별자</b>
     * <p>제공자 내에서 사용자를 유일하게 구분하는 ID입니다.</p>
     */
    private String oauthId;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public User(String name, String email, String picture, String oauthProvider, String oauthId) {
        this.name = name;
        this.email = email;
        this.picture = picture;
        this.oauthProvider = oauthProvider;
        this.oauthId = oauthId;
    }
}
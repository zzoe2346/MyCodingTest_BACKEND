package com.mycodingtest.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        // JwtUtil 인스턴스 생성
        jwtUtil = new JwtUtil();

        // @Value로 주입되는 값 설정
        String plainSecretKey = Base64.getEncoder().encodeToString("my-super-secret-key-for-jwt-testing".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtUtil, "plainSecretKey", plainSecretKey);
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L); // 1시간 (밀리초 단위)

        // @PostConstruct 수동 호출
        jwtUtil.init();
    }

    @Test
    void testGenerateToken() {
        // Given
        Long userId = 1L;
        String picture = "profile.jpg";
        String name = "testUser";

        // When
        String token = jwtUtil.generateToken(userId, picture, name);

        // Then
        assertNotNull(token);

        // 토큰 파싱하여 클레임 검증
        Claims claims = jwtUtil.extractAllClaims(token);
        assertEquals("api", claims.getSubject());
        assertEquals(userId, claims.get("userId", Long.class));
        assertEquals(name, claims.get("name", String.class));
        assertEquals(picture, claims.get("picture", String.class));

        // 발행 시간과 만료 시간 검증
        Date issuedAt = claims.getIssuedAt();
        Date expiration = claims.getExpiration();
        assertNotNull(issuedAt);
        assertNotNull(expiration);
        assertTrue(expiration.after(issuedAt));
        assertTrue(expiration.getTime() - issuedAt.getTime() <= 3600000L); // expiration 값과 일치
    }

    @Test
    void testExtractAllClaims() {
        // Given
        Long userId = 2L;
        String picture = "avatar.png";
        String name = "anotherUser";
        String token = jwtUtil.generateToken(userId, picture, name);

        // When
        Claims claims = jwtUtil.extractAllClaims(token);

        // Then
        assertNotNull(claims);
        assertEquals("api", claims.getSubject());
        assertEquals(userId, claims.get("userId", Long.class));
        assertEquals(name, claims.get("name", String.class));
        assertEquals(picture, claims.get("picture", String.class));
    }

//    @Test
//    void testExtractAllClaimsWithInvalidToken() {
//        // Given
//        String invalidToken = "invalid.token.here";
//
//        // When & Then
//        Exception exception = assertThrows(Exception.class, () -> {
//            jwtUtil.extractAllClaims(invalidToken);
//        });
//        System.out.println(exception.getMessage());
//
//        assertTrue(exception.getMessage().contains("JWT") || exception.getMessage().contains("signature"));
//    }
}
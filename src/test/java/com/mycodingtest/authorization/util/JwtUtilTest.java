package com.mycodingtest.authorization.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        String plainSecretKey = Base64.getEncoder().encodeToString("my-super-secret-key-for-jwt-testing".getBytes(StandardCharsets.UTF_8));
        ReflectionTestUtils.setField(jwtUtil, "plainSecretKey", plainSecretKey);
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);

        jwtUtil.init();
    }

    @Test
    void testGenerateToken() {
        // given
        Long userId = 1L;
        String picture = "profile.jpg";
        String name = "testUser";

        // when
        String token = jwtUtil.generateToken(userId, picture, name);

        // then
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
        // given
        Long userId = 2L;
        String picture = "avatar.png";
        String name = "anotherUser";
        String token = jwtUtil.generateToken(userId, picture, name);

        // when
        Claims claims = jwtUtil.extractAllClaims(token);

        // then
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
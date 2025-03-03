package com.mycodingtest.common.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    @DisplayName("Jwt 생성 한다")
    void testGenerateToken() {
        // given
        Long userId = 1L;
        String picture = "profile.jpg";
        String name = "testUser";

        // when
        String token = jwtUtil.generateToken(userId, picture, name);

        // then
        assertNotNull(token);
    }

    @Test
    @DisplayName("생성된 유효한 Jwt에서 클레임을 추출한다")
    void testExtractAllClaims() {
        // given
        Long userId = 2L;
        String picture = "picture";
        String name = "user";
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
}

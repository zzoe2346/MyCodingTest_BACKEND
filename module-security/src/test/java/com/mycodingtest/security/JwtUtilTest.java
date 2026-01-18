package com.mycodingtest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    // 테스트용 Base64 인코딩된 256비트 시크릿 키
    private static final String TEST_SECRET_KEY = "dGVzdFNlY3JldEtleUZvclVuaXRUZXN0aW5nMTIzNDU2Nzg5MA==";
    private static final Long TEST_EXPIRATION = 3600000L; // 1시간

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(jwtUtil, "plainSecretKey", TEST_SECRET_KEY);
        ReflectionTestUtils.setField(jwtUtil, "expiration", TEST_EXPIRATION);
        jwtUtil.init();
    }

    @Nested
    class 토큰_생성 {

        @Test
        void 사용자_정보로_JWT_토큰을_생성한다() {
            // given
            Long userId = 1L;
            String picture = "https://example.com/profile.jpg";
            String name = "홍길동";

            // when
            String token = jwtUtil.generateToken(userId, picture, name);

            // then
            assertThat(token).isNotNull();
            assertThat(token).isNotEmpty();
            assertThat(token.split("\\.")).hasSize(3); // JWT는 점으로 구분된 3부분
        }

        @Test
        void 생성된_토큰에서_Claims를_추출할_수_있다() {
            // given
            Long userId = 123L;
            String picture = "https://example.com/pic.jpg";
            String name = "테스트유저";

            // when
            String token = jwtUtil.generateToken(userId, picture, name);
            Claims claims = jwtUtil.extractAllClaims(token);

            // then
            assertThat(claims.get("userId", Long.class)).isEqualTo(123L);
            assertThat(claims.get("picture", String.class)).isEqualTo("https://example.com/pic.jpg");
            assertThat(claims.get("name", String.class)).isEqualTo("테스트유저");
            assertThat(claims.getSubject()).isEqualTo("api");
        }
    }

    @Nested
    class Claims_추출 {

        @Test
        void 유효한_토큰에서_userId를_추출한다() {
            // given
            String token = jwtUtil.generateToken(999L, "pic", "user");

            // when
            Claims claims = jwtUtil.extractAllClaims(token);

            // then
            assertThat(claims.get("userId", Long.class)).isEqualTo(999L);
        }

        @Test
        void 유효한_토큰에서_발급_시간이_포함되어_있다() {
            // given
            String token = jwtUtil.generateToken(1L, "pic", "user");

            // when
            Claims claims = jwtUtil.extractAllClaims(token);

            // then
            assertThat(claims.getIssuedAt()).isNotNull();
            assertThat(claims.getExpiration()).isNotNull();
            assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
        }

        @Test
        void 잘못된_토큰은_예외를_발생시킨다() {
            // given
            String invalidToken = "invalid.jwt.token";

            // when & then
            assertThatThrownBy(() -> jwtUtil.extractAllClaims(invalidToken))
                    .isInstanceOf(Exception.class);
        }

        @Test
        void 다른_시크릿_키로_서명된_토큰은_예외를_발생시킨다() {
            // given
            // 다른 시크릿 키로 토큰 생성
            String differentSecretKey = "YW5vdGhlclNlY3JldEtleUZvclVuaXRUZXN0aW5nMTIzNDU=";
            SecretKey differentKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(differentSecretKey));

            String tokenWithDifferentKey = Jwts.builder()
                    .subject("test")
                    .claim("userId", 1L)
                    .signWith(differentKey)
                    .compact();

            // when & then
            assertThatThrownBy(() -> jwtUtil.extractAllClaims(tokenWithDifferentKey))
                    .isInstanceOf(Exception.class);
        }
    }
}

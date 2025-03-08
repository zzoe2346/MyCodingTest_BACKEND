package com.mycodingtest.common.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CookieUtilTest {

    private CookieUtil cookieUtil;
    private final String COOKIE_NAME = "jwt";

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil();
        ReflectionTestUtils.setField(cookieUtil, "cookieName", COOKIE_NAME);
    }

    @Test
    @DisplayName("JWT 토큰이 담긴 쿠키를 생성한다")
    void generateJwtCookie() {
        // given
        String token = "test-jwt-token";

        // when
        ResponseCookie cookie = cookieUtil.generateJwtCookie(token);

        // then
        assertThat(cookie.getName()).isEqualTo(COOKIE_NAME);
        assertThat(cookie.getValue()).isEqualTo(token);
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecure()).isTrue();
        assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(60 * 60 * 24 * 90);
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getSameSite()).isEqualTo("None");
    }

    @Test
    @DisplayName("JWT 토큰이 담긴 쿠키를 요청에서 추출한다")
    void getJwtFromCookie() {

        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        String tokenValue = "test-jwt-token";
        Cookie[] cookies = {
                new Cookie("other", "value"),
                new Cookie(COOKIE_NAME, tokenValue)
        };
        given(request.getCookies()).willReturn(cookies);

        // when
        String extractedToken = cookieUtil.getJwtFromCookie(request);

        // then
        assertThat(extractedToken).isEqualTo(tokenValue);
    }

    @Test
    @DisplayName("JWT 쿠키가 없는 경우 null을 반환한다")
    void getJwtFromCookie_WhenNoCookie() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = {
                new Cookie("other", "value")
        };
        given(request.getCookies()).willReturn(cookies);

        // when
        String extractedToken = cookieUtil.getJwtFromCookie(request);

        // then
        assertThat(extractedToken).isNull();
    }

    @Test
    @DisplayName("JWT 쿠키를 제거한다")
    void generateClearJwtCookie() {
        // when
        ResponseCookie cookie = cookieUtil.generateClearJwtCookie();

        // then
        assertThat(cookie.getName()).isEqualTo(COOKIE_NAME);
        assertThat(cookie.getValue()).isEqualTo("");//null을 넣으면 ""로 value 가 됨!
        assertThat(cookie.isHttpOnly()).isTrue();
        assertThat(cookie.isSecure()).isTrue();
        assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(0);
        assertThat(cookie.getPath()).isEqualTo("/");
        assertThat(cookie.getSameSite()).isEqualTo("None");
    }

    @Test
    @DisplayName("요청에 쿠키가 아예 없는경우 null을 반환한다.")
    void getJwtFromCookie_WhenRequestNotContainCookie() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        given(request.getCookies()).willReturn(null);

        // when
        String extractedToken = cookieUtil.getJwtFromCookie(request);

        // then
        assertThat(extractedToken).isNull();
    }
}

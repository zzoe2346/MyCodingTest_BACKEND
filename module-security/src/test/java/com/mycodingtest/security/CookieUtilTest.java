package com.mycodingtest.security;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class CookieUtilTest {

    private CookieUtil cookieUtil;
    private final String testCookieName = "test_jwt_cookie";

    @BeforeEach
    void setUp() {
        cookieUtil = new CookieUtil();
        ReflectionTestUtils.setField(cookieUtil, "cookieName", testCookieName);
    }

    @Nested
    class JWT_쿠키_추출 {

        @Test
        void 쿠키가_있으면_JWT를_반환한다() {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie jwtCookie = new Cookie(testCookieName, "jwt_token_value");
            given(request.getCookies()).willReturn(new Cookie[] { jwtCookie });

            // when
            String result = cookieUtil.getJwtFromCookie(request);

            // then
            assertThat(result).isEqualTo("jwt_token_value");
        }

        @Test
        void 쿠키가_없으면_null을_반환한다() {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            given(request.getCookies()).willReturn(null);

            // when
            String result = cookieUtil.getJwtFromCookie(request);

            // then
            assertThat(result).isNull();
        }

        @Test
        void JWT_쿠키가_없으면_null을_반환한다() {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie otherCookie = new Cookie("other_cookie", "some_value");
            given(request.getCookies()).willReturn(new Cookie[] { otherCookie });

            // when
            String result = cookieUtil.getJwtFromCookie(request);

            // then
            assertThat(result).isNull();
        }

        @Test
        void 여러_쿠키_중_JWT_쿠키를_찾아_반환한다() {
            // given
            HttpServletRequest request = mock(HttpServletRequest.class);
            Cookie cookie1 = new Cookie("cookie1", "value1");
            Cookie jwtCookie = new Cookie(testCookieName, "correct_jwt");
            Cookie cookie3 = new Cookie("cookie3", "value3");
            given(request.getCookies()).willReturn(new Cookie[] { cookie1, jwtCookie, cookie3 });

            // when
            String result = cookieUtil.getJwtFromCookie(request);

            // then
            assertThat(result).isEqualTo("correct_jwt");
        }
    }

    @Nested
    class JWT_쿠키_생성 {

        @Test
        void JWT_쿠키를_올바르게_생성한다() {
            // given
            String token = "generated_jwt_token";

            // when
            ResponseCookie cookie = cookieUtil.generateJwtCookie(token);

            // then
            assertThat(cookie.getName()).isEqualTo(testCookieName);
            assertThat(cookie.getValue()).isEqualTo(token);
            assertThat(cookie.isHttpOnly()).isTrue();
            assertThat(cookie.isSecure()).isTrue();
            assertThat(cookie.getPath()).isEqualTo("/");
            assertThat(cookie.getSameSite()).isEqualTo("None");
            assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(60 * 60 * 24 * 90);
        }
    }

    @Nested
    class JWT_쿠키_삭제 {

        @Test
        void 삭제용_쿠키를_생성한다() {
            // given & when
            ResponseCookie cookie = cookieUtil.generateClearJwtCookie();

            // then
            assertThat(cookie.getName()).isEqualTo(testCookieName);
            assertThat(cookie.getValue()).isEmpty();
            assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(0);
            assertThat(cookie.isHttpOnly()).isTrue();
            assertThat(cookie.isSecure()).isTrue();
        }
    }
}

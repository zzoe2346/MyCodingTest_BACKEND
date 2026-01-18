package com.mycodingtest.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JwtFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    private JwtFilter jwtFilter;

    @BeforeEach
    void setUp() {
        jwtFilter = new JwtFilter(jwtUtil, cookieUtil);
        SecurityContextHolder.clearContext();
    }

    @Nested
    class 필터_동작 {

        @Test
        void 토큰이_있으면_SecurityContext에_인증정보를_설정한다() throws Exception {
            // given
            String token = "valid.jwt.token";
            given(cookieUtil.getJwtFromCookie(request)).willReturn(token);

            Claims claims = mock(Claims.class);
            given(claims.get("userId", Long.class)).willReturn(1L);
            given(claims.get("picture", String.class)).willReturn("https://example.com/pic.jpg");
            given(claims.get("name", String.class)).willReturn("홍길동");
            given(jwtUtil.extractAllClaims(token)).willReturn(claims);

            // when
            jwtFilter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(request, response);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication).isNotNull();
            assertThat(authentication.getPrincipal()).isInstanceOf(CustomUserDetails.class);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            assertThat(userDetails.getUserId()).isEqualTo(1L);
            assertThat(userDetails.getUsername()).isEqualTo("홍길동");
        }

        @Test
        void 토큰이_없으면_SecurityContext가_비어있다() throws Exception {
            // given
            given(cookieUtil.getJwtFromCookie(request)).willReturn(null);

            // when
            jwtFilter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(request, response);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            assertThat(authentication).isNull();
        }

        @Test
        void 항상_filterChain을_호출한다() throws Exception {
            // given
            given(cookieUtil.getJwtFromCookie(request)).willReturn(null);

            // when
            jwtFilter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(request, response);
        }
    }
}

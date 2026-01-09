package com.mycodingtest.security;

import com.mycodingtest.common.security.JwtFilter;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@Tag("unit")
@MockitoSettings
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
    @Mock
    private Claims claims;
    @Mock
    private SecurityContext securityContext;
    @InjectMocks
    private JwtFilter jwtFilter;

    @Test
    @DisplayName("JWT 토큰이 있을 경우 인증 컨텍스트를 설정한다")
    void doFilterInternal_withValidToken() throws ServletException, IOException {
        // given
        String token = "valid-token";
        Long userId = 1L;
        String picture = "profile.jpg";
        String name = "userName";

        given(cookieUtil.getJwtFromCookie(request)).willReturn(token);
        given(jwtUtil.extractAllClaims(token)).willReturn(claims);
        given(claims.get("userId", Long.class)).willReturn(userId);
        given(claims.get("picture", String.class)).willReturn(picture);
        given(claims.get("name", String.class)).willReturn(name);

        // SecurityContext 모킹
        try (var securityContextMock = mockStatic(SecurityContextHolder.class)) {
            securityContextMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            given(securityContext.getAuthentication()).willReturn(null);

            // when
            jwtFilter.doFilterInternal(request, response, filterChain);

            // then
            securityContextMock.verify(SecurityContextHolder::getContext, times(2));
            verify(securityContext).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    @DisplayName("JWT 토큰이 없을 경우 인증 컨텍스트를 설정하지 않는다")
    void doFilterInternal_withoutToken() throws ServletException, IOException {
        // given
        given(cookieUtil.getJwtFromCookie(request)).willReturn(null);

        // when
        jwtFilter.doFilterInternal(request, response, filterChain);

        // then
        verify(filterChain).doFilter(request, response);
        verify(jwtUtil, never()).extractAllClaims(anyString());
    }

    @Test
    @DisplayName("인증 컨텍스트가 이미 설정되어 있을 경우 처리하지 않는다")
    void doFilterInternal_withExistingAuthentication() throws ServletException, IOException {
        // given
        String token = "valid-token";
        given(cookieUtil.getJwtFromCookie(request)).willReturn(token);

        // SecurityContext 모킹
        try (var securityContextMock = mockStatic(SecurityContextHolder.class)) {
            securityContextMock.when(SecurityContextHolder::getContext).thenReturn(securityContext);
            given(securityContext.getAuthentication()).willReturn(mock(Authentication.class));

            // when
            jwtFilter.doFilterInternal(request, response, filterChain);

            // then
            verify(filterChain).doFilter(request, response);
            verify(jwtUtil, never()).extractAllClaims(anyString());
            verify(securityContext, never()).setAuthentication(any());
        }
    }
}

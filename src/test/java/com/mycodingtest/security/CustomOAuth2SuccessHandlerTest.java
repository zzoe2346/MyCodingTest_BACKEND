package com.mycodingtest.security;

import com.mycodingtest.authorization.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MockitoSettings
class CustomOAuth2SuccessHandlerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private CustomOAuth2SuccessHandler successHandler;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(successHandler, "redirectUrl", "http://localhost:3000");
    }

    @Test
    @DisplayName("인증 성공 시 쿠키를 설정하고 리다이렉트한다")
    void onAuthenticationSuccess() throws IOException {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // OAuth2User 객체 생성
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("name", "testUser");
        attributes.put("email", "test@example.com");
        OAuth2User oAuth2User = new DefaultOAuth2User(
                Set.of(), attributes, "name");

        // OAuth2AuthenticationToken 생성
        String provider = "google";
        OAuth2AuthenticationToken authentication = mock(OAuth2AuthenticationToken.class);
        given(authentication.getPrincipal()).willReturn(oAuth2User);
        given(authentication.getAuthorizedClientRegistrationId()).willReturn(provider);

        // JWT Cookie 생성 모킹
        ResponseCookie jwtCookie = ResponseCookie.from("JWT", "test-token")
                .httpOnly(true)
                .path("/")
                .build();
        given(authService.handleOAuth2Login(any(OAuth2User.class), eq(provider)))
                .willReturn(jwtCookie);

        // when
        successHandler.onAuthenticationSuccess(request, response, authentication);

        // then
        verify(response).addHeader(eq(HttpHeaders.SET_COOKIE), eq(jwtCookie.toString()));
        verify(response).sendRedirect(eq("http://localhost:3000"));
    }
}

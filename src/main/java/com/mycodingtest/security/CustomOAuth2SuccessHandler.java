package com.mycodingtest.security;

import com.mycodingtest.authorization.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${url.redirect}")
    private String redirectUrl;

    private final AuthService authService;

    public CustomOAuth2SuccessHandler(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = authToken.getPrincipal();
        String provider = authToken.getAuthorizedClientRegistrationId();

        // 인증 처리 및 JWT Cookie 생성
        ResponseCookie jwtCookie = authService.handleOAuth2Login(oauth2User, provider);

        response.addHeader(HttpHeaders.SET_COOKIE, jwtCookie.toString());
        response.sendRedirect(redirectUrl);

        //SecurityContext 즉시 초기화 (세션 제거)
        SecurityContextHolder.clearContext();
    }
}

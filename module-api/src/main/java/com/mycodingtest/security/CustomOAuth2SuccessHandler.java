package com.mycodingtest.security;

import com.mycodingtest.authorization.CookieUtil;
import com.mycodingtest.authorization.JwtUtil;
import com.mycodingtest.domain.user.User;
import com.mycodingtest.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${url.redirect}")
    private String redirectUrl;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    public CustomOAuth2SuccessHandler(UserService userService, JwtUtil jwtUtil, CookieUtil cookieUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = authToken.getPrincipal();
        String provider = authToken.getAuthorizedClientRegistrationId();

        // 인증 처리 및 JWT Cookie 생성
        String email, name, picture, oauthId;

        if ("google".equals(provider)) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
            picture = oauth2User.getAttribute("picture");
            oauthId = oauth2User.getAttribute("sub");
        } else if ("kakao".equals(provider)) {
            Map<String, Object> properties = oauth2User.getAttribute("properties");
            email = "no mail";
            name = (String) properties.get("nickname");
            picture = (String) properties.get("thumbnail_image");
            oauthId = String.valueOf(oauth2User.getAttributes().get("id"));
        } else {
            throw new RuntimeException("지원하는 OAuth2 제공자가 아닙니다.");
        }

        //뭐 가입안됭ㅆ으면 가입시깈낟.
        User user = userService.getOrCreateUser(name, email, picture, provider, oauthId);
        String jwt = jwtUtil.generateToken(user.getId(), user.getPicture(), user.getName());
        response.addHeader(HttpHeaders.SET_COOKIE, cookieUtil.generateJwtCookie(jwt).toString());
        response.sendRedirect(redirectUrl);

        //SecurityContext 즉시 초기화 (세션 제거)
        SecurityContextHolder.clearContext();
    }
}

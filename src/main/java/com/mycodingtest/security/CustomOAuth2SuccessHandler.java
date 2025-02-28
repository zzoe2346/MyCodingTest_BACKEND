package com.mycodingtest.security;

import com.mycodingtest.authorization.util.JwtUtil;
import com.mycodingtest.user.User;
import com.mycodingtest.user.UserRepository;
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
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${url.redirect}")
    private String redirectUrl;
    @Value("${cookie.name}")
    private String cookieName;

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public CustomOAuth2SuccessHandler(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2AuthenticationToken authToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oauth2User = authToken.getPrincipal();
        String provider = authToken.getAuthorizedClientRegistrationId();

        String email;
        String name;
        String picture;
        String oauthId;

        if (provider.equals("google")) {
            email = oauth2User.getAttribute("email");
            name = oauth2User.getAttribute("name");
            picture = oauth2User.getAttribute("picture");
            oauthId = oauth2User.getAttribute("sub");
        } else if (provider.equals("kakao")) {
            Map<String, Object> properties = oauth2User.getAttribute("properties");
            email = "no mail";
            name = (String) properties.get("nickname");
            picture = (String) properties.get("thumbnail_image");
            oauthId = String.valueOf(oauth2User.getAttributes().get("id"));
        } else {
            throw new RuntimeException("지원하는 oauth2 제공자가 아닙니다.");
        }

        User user = userRepository.findByOauthProviderAndOauthId(provider, oauthId)
                .orElseGet(() -> userRepository.save(new User(name, email, picture, provider, oauthId)));

        String token = jwtUtil.generateToken(user.getId(), user.getPicture(), name);
        ResponseCookie cookie = ResponseCookie.from(cookieName, token)
                .httpOnly(true)
                .maxAge(60 * 60 * 24 * 90)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        response.sendRedirect(redirectUrl);

        //SecurityContext 즉시 초기화 (세션 제거)
        SecurityContextHolder.clearContext();
    }
}

package com.mycodingtest.authorization;

import com.mycodingtest.authorization.dto.UserInfoResponse;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.user.User;
import com.mycodingtest.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    public AuthService(CookieUtil cookieUtil, JwtUtil jwtUtil, UserService userService) {
        this.cookieUtil = cookieUtil;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    public void signOut(HttpServletResponse response) {
        ResponseCookie cookieForClear = cookieUtil.clearJwtCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieForClear.toString());
    }

    public UserInfoResponse generateUserInfo(String picture, String username) {
        return new UserInfoResponse(picture, username);
    }

    public ResponseCookie handleOAuth2Login(OAuth2User oauth2User, String provider) {
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

        // 데이터베이스에 존재하는 유저인지 확인 없으면 등록
        User user = userService.findByOauthProviderAndOauthId(provider, oauthId)
                .orElseGet(() -> userService.enrollNewUser(name, email, picture, provider, oauthId));

        // jwt 생성
        String jwt = jwtUtil.generateToken(user.getId(), user.getPicture(), user.getName());
        // jwt가진 쿠키 생성
        return cookieUtil.generateJwtCookie(jwt);
    }
}

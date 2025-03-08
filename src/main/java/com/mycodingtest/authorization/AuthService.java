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

    /**
     * 서버에서 발급한 쿠키를 바로 만료시키는 로그아웃용 clear 쿠키를 발급하여 HttpServletResponse의 헤더에 add 시킨다.
     * @param response 이 파라미터에 만료용 쿠키를 add 시킨다.
     */
    public void signOut(HttpServletResponse response) {
        ResponseCookie cookieForClear = cookieUtil.generateClearJwtCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieForClear.toString());
    }

    /**
     * 유저의 소셜계정의 프로필 url, 이름 정보를 담은 UserInfoResponse를 반환한다.
     * @param picture 소셜 계정 프로필 url
     * @param username 유저의 이름
     * @return UserInfoResponse 응답용 DTO 객체 반환
     */
    public UserInfoResponse generateUserInfo(String picture, String username) {
        return new UserInfoResponse(picture, username);
    }

    /**
     * 1. 소셜 로그인 제공자에 따른 유저 정보 추출<br>
     * 2. DB에 유저 정보 존재확인. 없으면 새로 등록시킴<br>
     * 3. JWT Cookie 만들어 반환<br>
     * @param oauth2User OAuth2User
     * @param provider 소셜 로그인 제공자
     * @return 별 문제 없으면 ResponseCookie 반환
     */
    public ResponseCookie handleOAuth2Login(OAuth2User oauth2User, String provider) {
        String email, name, picture, oauthId;

        // social provider에 따라서 사용자 정보를 추출
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

        // 데이터베이스에 존재하는 유저인지 확인. 없으면 새로 등록
        User user = userService.findByOauthProviderAndOauthId(provider, oauthId)
                .orElseGet(() -> userService.enrollNewUser(name, email, picture, provider, oauthId));

        // jwt 생성
        String jwt = jwtUtil.generateToken(user.getId(), user.getPicture(), user.getName());

        // jwt 쿠키 생성
        return cookieUtil.generateJwtCookie(jwt);
    }
}

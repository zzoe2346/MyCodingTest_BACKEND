package com.mycodingtest.authorization;

import com.mycodingtest.authorization.dto.UserInfoResponse;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.application.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <h3>인증 및 인가 서비스 (AuthService)</h3>
 * <p>
 * 소셜 로그인 처리, JWT 발급, 세션(쿠키) 관리 등 보안과 관련된 비즈니스 로직을 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final CookieUtil cookieUtil;
    private final JwtUtil jwtUtil;
    private final UserService userService;

    /**
     * 로그아웃 처리를 위해 만료된 JWT 쿠키를 브라우저에 전송합니다.
     * @param response HttpServletResponse
     */
    public void signOut(HttpServletResponse response) {
        ResponseCookie cookieForClear = cookieUtil.generateClearJwtCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookieForClear.toString());
    }

    /**
     * 사용자 프로필 응답 DTO를 생성합니다.
     */
    public UserInfoResponse generateUserInfo(String picture, String username) {
        return new UserInfoResponse(picture, username);
    }

    /**
     * <b>OAuth2 로그인 통합 처리 로직</b>
     * <ol>
     *     <li>소셜 공급자로부터 유저 정보를 추출합니다.</li>
     *     <li>기존 회원이 아니면 신규 회원으로 자동 가입(Enroll) 처리합니다.</li>
     *     <li>사용자 식별 정보를 포함한 JWT를 생성합니다.</li>
     *     <li>보안이 강화된(HttpOnly) 쿠키 형태로 JWT를 반환합니다.</li>
     * </ol>
     * 
     * @param oauth2User 소셜 공급자로부터 받은 유저 상세 객체
     * @param provider 소셜 공급자 명칭 (google, kakao 등)
     * @return JWT가 포함된 보안 쿠키
     */
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

        User user = userService.findByOauthProviderAndOauthId(provider, oauthId)
                .orElseGet(() -> userService.enrollNewUser(name, email, picture, provider, oauthId));

        String jwt = jwtUtil.generateToken(user.getId(), user.getPicture(), user.getName());

        return cookieUtil.generateJwtCookie(jwt);
    }
}
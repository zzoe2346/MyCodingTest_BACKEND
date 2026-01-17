package com.mycodingtest.authorization;

import lombok.RequiredArgsConstructor;

/**
 * <h3>인증 및 인가 서비스 (AuthService)</h3>
 * <p>
 * 소셜 로그인 처리, JWT 발급, 세션(쿠키) 관리 등 보안과 관련된 비즈니스 로직을 수행합니다.
 * </p>
 */

@RequiredArgsConstructor
public class AuthService {
//
//    private final UserService userService;
//
//    /**
//     * 사용자 로그인을 처리하고 JWT 토큰 문자열을 반환합니다.
//     * (쿠키 생성 및 응답 헤더 추가는 API 레이어의 책임입니다.)
//     */
//    public String login(OAuthLoginCommand command) {
//        User user = userService.findUser(command.provider(), command.oauthId())
//                .orElseGet(() -> userService.getOrCreateUser(
//                        command.name(),
//                        command.email(),
//                        command.picture(),
//                        command.provider(),
//                        command.oauthId()
//                ));
//
//        // 순수 JWT 문자열만 생성하여 반환
//        return jwtUtil.generateToken(user.getId(), user.getPicture(), user.getName());
//    }

}
//
//    /**
//     * 사용자 프로필 응답 DTO를 생성합니다.
//     */
//    public UserInfoResponse generateUserInfo(String picture, String username) {
//        return new UserInfoResponse(picture, username);
//    }
//
//    /**
//     * <b>OAuth2 로그인 통합 처리 로직</b>
//     * <ol>
//     *     <li>소셜 공급자로부터 유저 정보를 추출합니다.</li>
//     *     <li>기존 회원이 아니면 신규 회원으로 자동 가입(Enroll) 처리합니다.</li>
//     *     <li>사용자 식별 정보를 포함한 JWT를 생성합니다.</li>
//     *     <li>보안이 강화된(HttpOnly) 쿠키 형태로 JWT를 반환합니다.</li>
//     * </ol>
//     *
//     * @param oauth2User 소셜 공급자로부터 받은 유저 상세 객체
//     * @param provider 소셜 공급자 명칭 (google, kakao 등)
//     * @return JWT가 포함된 보안 쿠키
//     */
//    public ResponseCookie handleOAuth2Login(OAuth2User oauth2User, String provider) {
//        String email, name, picture, oauthId;
//
//        if ("google".equals(provider)) {
//            email = oauth2User.getAttribute("email");
//            name = oauth2User.getAttribute("name");
//            picture = oauth2User.getAttribute("picture");
//            oauthId = oauth2User.getAttribute("sub");
//        } else if ("kakao".equals(provider)) {
//            Map<String, Object> properties = oauth2User.getAttribute("properties");
//            email = "no mail";
//            name = (String) properties.get("nickname");
//            picture = (String) properties.get("thumbnail_image");
//            oauthId = String.valueOf(oauth2User.getAttributes().get("id"));
//        } else {
//            throw new RuntimeException("지원하는 OAuth2 제공자가 아닙니다.");
//        }
//
//        User user = userService.findByOauthProviderAndOauthId(provider, oauthId)
//                .orElseGet(() -> userService.getOrCreateUser(name, email, picture, provider, oauthId));
//
//        String jwt = jwtUtil.generateToken(user.getId(), user.getPicture(), user.getName());
//
//        return cookieUtil.generateJwtCookie(jwt);
//    }


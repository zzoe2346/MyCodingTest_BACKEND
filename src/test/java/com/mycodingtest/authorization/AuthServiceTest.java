package com.mycodingtest.authorization;

import com.mycodingtest.authorization.dto.UserInfoResponse;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.user.User;
import com.mycodingtest.user.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@MockitoSettings
class AuthServiceTest {

    @Mock
    private CookieUtil cookieUtil;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserService userService;
    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("로그아웃 시 쿠키를 제거한다")
    void signOut() {
        // given
        ResponseCookie clearCookie = ResponseCookie.from("jwt", "").build();
        given(cookieUtil.generateClearJwtCookie()).willReturn(clearCookie);

        // when
        authService.signOut(response);

        // then
        verify(response).addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
    }

    @Test
    @DisplayName("사용자 정보를 조회한다")
    void generateUserInfo() {
        // given
        String picture = "testPicture";
        String username = "testName";

        // when
        UserInfoResponse userInfo = authService.generateUserInfo(picture, username);

        // then
        assertThat(userInfo.picture()).isEqualTo(picture);
        assertThat(userInfo.name()).isEqualTo(username);
    }

    @Nested
    class HandleOAuth2LoginTest {
        @Test
        @DisplayName("구글 OAuth2 로그인 처리 - 신규 사용자")
        void handleOAuth2Login_Google_NewUser() {
            // given
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", "test@test.com");
            attributes.put("name", "testName");
            attributes.put("picture", "testPicture");
            attributes.put("sub", "11223344");
            OAuth2User oauth2User = new DefaultOAuth2User(null, attributes, "sub");

            User newUser = mock(User.class);
            given(newUser.getId()).willReturn(1L);
            given(newUser.getPicture()).willReturn("testPicture");
            given(newUser.getName()).willReturn("testName");

            // 데이터베이스에 없음
            given(userService.findByOauthProviderAndOauthId("google", "11223344"))
                    .willReturn(Optional.empty());
            // 신규 회원 등록
            given(userService.enrollNewUser("testName", "test@test.com", "testPicture", "google", "11223344"))
                    .willReturn(newUser);
            // jwt 생성
            given(jwtUtil.generateToken(newUser.getId(), newUser.getPicture(), newUser.getName()))
                    .willReturn("jwt");
            // 쿠키 생성
            given(cookieUtil.generateJwtCookie("jwt"))
                    .willReturn(ResponseCookie.from("cookieName", "jwt").build());
            // when
            ResponseCookie cookie = authService.handleOAuth2Login(oauth2User, "google");

            // then
            assertThat(cookie.getValue()).isEqualTo("jwt");
        }

        @Test
        @DisplayName("구글 OAuth2 로그인 처리 - 기존 사용자")
        void handleOAuth2Login_Google_ExistUser() {
            // given
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", "test@test.com");
            attributes.put("name", "testName");
            attributes.put("picture", "testPicture");
            attributes.put("sub", "11223344");
            OAuth2User oauth2User = new DefaultOAuth2User(null, attributes, "sub");

            User existUser = mock(User.class);
            given(existUser.getId()).willReturn(1L);
            given(existUser.getPicture()).willReturn("testPicture");
            given(existUser.getName()).willReturn("testName");

            // 데이터베이스에 있음
            given(userService.findByOauthProviderAndOauthId("google", "11223344"))
                    .willReturn(Optional.of(existUser));
            // jwt 생성
            given(jwtUtil.generateToken(existUser.getId(), existUser.getPicture(), existUser.getName()))
                    .willReturn("jwt");
            // 쿠키 생성
            given(cookieUtil.generateJwtCookie("jwt"))
                    .willReturn(ResponseCookie.from("cookieName", "jwt").build());
            // when
            ResponseCookie cookie = authService.handleOAuth2Login(oauth2User, "google");

            // then
            assertThat(cookie.getValue()).isEqualTo("jwt");
        }

        @Test
        @DisplayName("카카오 OAuth2 로그인 처리 - 신규 사용자")
        void handleOAuth2Login_Kakao_NewUser() {
            // given
            Map<String, Object> properties = new HashMap<>();
            properties.put("nickname", "testName");
            properties.put("thumbnail_image", "testPicture");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", "11223344");
            attributes.put("properties", properties);
            OAuth2User oauth2User = new DefaultOAuth2User(null, attributes, "id");

            User newUser = mock(User.class);
            given(newUser.getId()).willReturn(1L);
            given(newUser.getPicture()).willReturn("testPicture");
            given(newUser.getName()).willReturn("testName");

            // 데이터베이스에 없음
            given(userService.findByOauthProviderAndOauthId("kakao", "11223344"))
                    .willReturn(Optional.empty());
            // 신규 회원 등록
            given(userService.enrollNewUser("testName", "no mail", "testPicture", "kakao", "11223344"))
                    .willReturn(newUser);
            // jwt 생성
            given(jwtUtil.generateToken(newUser.getId(), newUser.getPicture(), newUser.getName()))
                    .willReturn("jwt");
            // 쿠키 생성
            given(cookieUtil.generateJwtCookie("jwt"))
                    .willReturn(ResponseCookie.from("cookieName", "jwt").build());
            // when
            ResponseCookie cookie = authService.handleOAuth2Login(oauth2User, "kakao");

            // then
            assertThat(cookie.getValue()).isEqualTo("jwt");
        }

        @Test
        @DisplayName("카카오 OAuth2 로그인 처리 - 기존 사용자")
        void handleOAuth2Login_Kakao_ExistingUser() {
            // given
            Map<String, Object> properties = new HashMap<>();
            properties.put("nickname", "testName");
            properties.put("thumbnail_image", "testPicture");
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("id", "11223344");
            attributes.put("properties", properties);
            OAuth2User oauth2User = new DefaultOAuth2User(null, attributes, "id");

            User existUser = mock(User.class);
            given(existUser.getId()).willReturn(1L);
            given(existUser.getPicture()).willReturn("testPicture");
            given(existUser.getName()).willReturn("testName");

            // 데이터베이스에 존재
            given(userService.findByOauthProviderAndOauthId("kakao", "11223344"))
                    .willReturn(Optional.of(existUser));
            // jwt 생성
            given(jwtUtil.generateToken(existUser.getId(), existUser.getPicture(), existUser.getName()))
                    .willReturn("jwt");
            // 쿠키 생성
            given(cookieUtil.generateJwtCookie("jwt"))
                    .willReturn(ResponseCookie.from("cookieName", "jwt").build());
            // when
            ResponseCookie cookie = authService.handleOAuth2Login(oauth2User, "kakao");

            // then
            assertThat(cookie.getValue()).isEqualTo("jwt");
        }

        @Test
        @DisplayName("지원하지 않는 OAuth2 제공자로 로그인 시 예외가 발생한다")
        void handleOAuth2Login_UnsupportedProvider() {
            // given
            Map<String, Object> attributes = new HashMap<>();
            attributes.put("email", "test@test.com");
            attributes.put("name", "testName");
            attributes.put("picture", "testPicture");
            attributes.put("sub", "11223344");
            OAuth2User oauth2User = new DefaultOAuth2User(null, attributes, "sub");

            // when then
            assertThrows(RuntimeException.class, () ->
                    authService.handleOAuth2Login(oauth2User, "unsupported"));
        }
    }
}

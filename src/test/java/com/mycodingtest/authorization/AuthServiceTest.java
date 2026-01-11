package com.mycodingtest.authorization;

import com.mycodingtest.authorization.dto.UserInfoResponse;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.user.application.UserService;
import com.mycodingtest.user.domain.User;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private CookieUtil cookieUtil;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletResponse response;

    @Mock
    private OAuth2User oAuth2User;

    @Test
    void 로그아웃_성공_시_쿠키_만료_헤더_추가() {
        // given
        ResponseCookie clearCookie = ResponseCookie.from("accessToken", "")
                .maxAge(0)
                .build();
        given(cookieUtil.generateClearJwtCookie()).willReturn(clearCookie);

        // when
        authService.signOut(response);

        // then
        verify(response).addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
    }

    @Test
    void 유저_정보_생성_성공() {
        // given
        String picture = "http://picture.url";
        String username = "testUser";

        // when
        UserInfoResponse userInfo = authService.generateUserInfo(picture, username);

        // then
        assertThat(userInfo.picture()).isEqualTo(picture);
        assertThat(userInfo.name()).isEqualTo(username);
    }

    @Test
    void 구글_로그인_신규_유저_성공() {
        // given
        String provider = "google";
        String email = "test@gmail.com";
        String name = "Google User";
        String picture = "http://google.com/pic";
        String oauthId = "12345";

        given(oAuth2User.getAttribute("email")).willReturn(email);
        given(oAuth2User.getAttribute("name")).willReturn(name);
        given(oAuth2User.getAttribute("picture")).willReturn(picture);
        given(oAuth2User.getAttribute("sub")).willReturn(oauthId);

        given(userService.findByOauthProviderAndOauthId(provider, oauthId))
                .willReturn(Optional.empty());

        User newUser = new User(name, email, picture, provider, oauthId);
        // Reflection to set ID for testing if needed, or just rely on object reference
        // Assuming ID is null for new user until saved, but service returns the saved user usually.
        // Mocking enrollNewUser to return a user with ID.
        given(userService.enrollNewUser(name, email, picture, provider, oauthId))
                .willReturn(newUser);

        String token = "jwt-token";
        given(jwtUtil.generateToken(any(), any(), any())).willReturn(token);

        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", token).build();
        given(cookieUtil.generateJwtCookie(token)).willReturn(jwtCookie);

        // when
        ResponseCookie result = authService.handleOAuth2Login(oAuth2User, provider);

        // then
        assertThat(result).isEqualTo(jwtCookie);
        verify(userService).enrollNewUser(name, email, picture, provider, oauthId);
    }

    @Test
    void 카카오_로그인_기존_유저_성공() {
        // given
        String provider = "kakao";
        String name = "Kakao User";
        String picture = "http://kakao.com/pic";
        String oauthId = "67890";
        Map<String, Object> properties = Map.of("nickname", name, "thumbnail_image", picture);
        Map<String, Object> attributes = Map.of("id", 67890L); // Integer or Long usually

        given(oAuth2User.getAttribute("properties")).willReturn(properties);
        given(oAuth2User.getAttributes()).willReturn(attributes);

        User existingUser = new User(name, "no mail", picture, provider, oauthId);
        given(userService.findByOauthProviderAndOauthId(provider, oauthId))
                .willReturn(Optional.of(existingUser));

        String token = "jwt-token";
        given(jwtUtil.generateToken(any(), any(), any())).willReturn(token);

        ResponseCookie jwtCookie = ResponseCookie.from("accessToken", token).build();
        given(cookieUtil.generateJwtCookie(token)).willReturn(jwtCookie);

        // when
        ResponseCookie result = authService.handleOAuth2Login(oAuth2User, provider);

        // then
        assertThat(result).isEqualTo(jwtCookie);
        verify(userService).findByOauthProviderAndOauthId(provider, oauthId);
    }

    @Test
    void 지원하지_않는_제공자_로그인_실패() {
        // given
        String provider = "facebook";

        // when & then
        assertThatThrownBy(() -> authService.handleOAuth2Login(oAuth2User, provider))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("지원하는 OAuth2 제공자가 아닙니다.");
    }
}

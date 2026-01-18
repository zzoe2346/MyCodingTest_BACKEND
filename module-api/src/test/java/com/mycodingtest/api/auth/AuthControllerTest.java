package com.mycodingtest.api.auth;

import com.mycodingtest.security.CookieUtil;
import com.mycodingtest.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private CookieUtil cookieUtil;

    @InjectMocks
    private AuthController authController;

    @Nested
    class 로그아웃 {

        @Test
        void 쿠키를_삭제하고_OK를_반환한다() {
            // given
            HttpServletResponse response = mock(HttpServletResponse.class);
            ResponseCookie clearCookie = ResponseCookie.from("jwt", "")
                    .maxAge(0)
                    .build();
            given(cookieUtil.generateClearJwtCookie()).willReturn(clearCookie);

            // when
            ResponseEntity<Void> result = authController.signOut(response);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            verify(cookieUtil).generateClearJwtCookie();
            verify(response).addHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());
        }
    }

    @Nested
    class 로그인_상태_확인 {

        @Test
        void 사용자_정보를_반환한다() {
            // given
            Long userId = 1L;
            String picture = "https://example.com/profile.jpg";
            String username = "홍길동";
            CustomUserDetails userDetails = new CustomUserDetails(userId, picture, username);

            // when
            var result = authController.checkSignIn(userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().picture()).isEqualTo(picture);
            assertThat(result.getBody().name()).isEqualTo(username);
        }
    }
}

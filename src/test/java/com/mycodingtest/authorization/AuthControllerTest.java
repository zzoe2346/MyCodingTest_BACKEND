package com.mycodingtest.authorization;

import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private CookieUtil cookieUtil;

    @Test
    @WithMockUser
    @DisplayName("로그아웃 성공시 200 상태코드를 반환한다")
    void signOut() throws Exception {
        // given
        doNothing().when(authService).signOut(any(HttpServletResponse.class));

        // when
        mockMvc.perform(get("/api/sign-out"))
                .andExpect(status().isOk());

        //then
        verify(authService, times(1)).signOut(any(HttpServletResponse.class));
    }

    @Test
    @WithMockUser
    @DisplayName("인증이 완료된 유저가 로그인를 체크하면 UserInfo(이름, 이미지)를 반환한다.")
    void checkMe_success() throws Exception {
        // given
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "name");
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        TestSecurityContextHolder.setAuthentication(authToken);


        // when
        mockMvc.perform(get("/api/me"))
                .andExpect(status().isOk());

        // then
        verify(authService, times(1)).getUserInfo("picture", "name");
    }
}

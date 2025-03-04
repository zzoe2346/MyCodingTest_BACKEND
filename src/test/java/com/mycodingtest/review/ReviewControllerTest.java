package com.mycodingtest.review;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.review.dto.ReviewRecentStatusResponse;
import com.mycodingtest.review.dto.ReviewResponse;
import com.mycodingtest.review.dto.WaitReviewCountResponse;
import com.mycodingtest.security.CustomUserDetails;
import com.mycodingtest.storage.dto.UrlResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;

    @MockitoBean
    private CookieUtil cookieUtil;

    @MockitoBean
    private ReviewService reviewService;

    @BeforeEach
    void autoSetting() {
        // 인증 완료된 유저의 요청임을 보장하고 테스트 시행
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "name");
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        TestSecurityContextHolder.setAuthentication(authToken);
    }

    @Test
    @WithMockUser
    @DisplayName("체감 난이도, 재복습 필요도를 수정한다")
    void updateReviewRatingLevels() throws Exception {
        // given
        ReviewRatingLevelsUpdateRequest request = new ReviewRatingLevelsUpdateRequest(3, 2);

        // when & then
        mockMvc.perform(put("/api/solved-problems/reviews/1/levels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)).with(csrf()))
                .andExpect(status().isOk());

        verify(reviewService).updateReviewRatingLevels(eq(request), eq(1L), eq(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("리뷰 정보를 조회한다")
    void getReview() throws Exception {
        // given
        ReviewResponse response = new ReviewResponse(3, 3, true, LocalDateTime.now());
        given(reviewService.getReview(1L, 1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/solved-problems/reviews/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewed").value(true));
    }

    @Test
    @WithMockUser
    @DisplayName("메모 수정 URL을 조회한다")
    void getMemoUpdateUrl() throws Exception {
        // given
        String expectedUrl = "https://example.com/update";
        given(reviewService.getMemoUpdateUrl(1L, 1L)).willReturn(new UrlResponse(expectedUrl));

        // when & then
        mockMvc.perform(get("/api/solved-problems/reviews/1/memo/update"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(expectedUrl));
    }

    @Test
    @WithMockUser
    @DisplayName("메모 읽기 URL을 조회한다")
    void getMemoReadUrl() throws Exception {
        // given
        String expectedUrl = "https://example.com/read";
        given(reviewService.getMemoReadUrl(1L, 1L)).willReturn(new UrlResponse(expectedUrl));

        // when & then
        mockMvc.perform(get("/api/solved-problems/reviews/1/memo/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(expectedUrl));
    }

    @Test
    @WithMockUser
    @DisplayName("리뷰 상태를 완료로 전환한다")
    void updateReviewStatus() throws Exception {
        // given
        ReviewRecentStatusResponse response = new ReviewRecentStatusResponse(true, LocalDateTime.now());
        given(reviewService.updateReviewStatus(1L, 1L)).willReturn(response);

        // when & then
        mockMvc.perform(put("/api/solved-problems/reviews/1/status").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reviewed").value(true));
    }

    @Test
    @WithMockUser
    @DisplayName("리뷰를 기다리는 문제 개수를 반환한다")
    void getWaitReviewCount() throws Exception {
        // given
        WaitReviewCountResponse response = new WaitReviewCountResponse(5);
        given(reviewService.getWaitReviewCount(1L)).willReturn(response);

        // when & then
        mockMvc.perform(get("/api/solved-problems/unreviewed-count"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.count").value(5));
    }
}
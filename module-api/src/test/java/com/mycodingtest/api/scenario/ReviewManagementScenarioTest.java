package com.mycodingtest.api.scenario;

import com.mycodingtest.api.collector.dto.CreateProblemAndJudgmentRequest;
import com.mycodingtest.api.review.dto.request.UpdateReviewRequest;
import com.mycodingtest.api.scenario.support.WithMockCustomUser;
import com.mycodingtest.domain.review.ReviewStatus;
import com.mycodingtest.security.CustomUserDetails;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 리뷰 관리 플로우 시나리오 통합 테스트.
 */
@Transactional
@SuppressWarnings("NonAsciiCharacters")
class ReviewManagementScenarioTest extends ScenarioTestBase {

    @Nested
    class 리뷰_관리_시나리오 {

        @Test
        @WithMockCustomUser
        void 채점_결과_제출_시_리뷰가_자동_생성되고_조회할_수_있다() throws Exception {
            // given: 채점 결과 제출 데이터
            CreateProblemAndJudgmentRequest request = new CreateProblemAndJudgmentRequest(
                    2000, "테스트 문제", 200001L,
                    "testUser", "맞았습니다!!", 1024, 100, "Java 11", 150,
                    LocalDateTime.now(), "public class Main { }");

            // 1단계: 채점 결과 제출 (리뷰 자동 생성)
            mockMvc.perform(post("/api/boj")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // 2단계: 리뷰 목록 조회 - TO_DO 상태 리뷰 확인
            mockMvc.perform(get("/api/reviews?filter=TO_DO")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))));

            // 3단계: 미완료 리뷰 개수 조회
            mockMvc.perform(get("/api/reviews/unreviewed/count"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.count", greaterThanOrEqualTo(1)));
        }

        @Test
        @WithMockCustomUser
        void 리뷰를_수정하고_변경된_내용을_확인할_수_있다() throws Exception {
            // given: 먼저 채점 결과를 제출하여 리뷰 생성
            CreateProblemAndJudgmentRequest submitRequest = new CreateProblemAndJudgmentRequest(
                    2001, "수정 테스트 문제", 200002L,
                    "testUser", "맞았습니다!!", 1024, 100, "Java 11", 150,
                    LocalDateTime.now(), "public class Solution { }");

            mockMvc.perform(post("/api/boj")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(submitRequest)))
                    .andExpect(status().isCreated());

            // 리뷰 목록에서 생성된 리뷰 ID 확인
            String responseBody = mockMvc.perform(get("/api/reviews?filter=TO_DO"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(greaterThanOrEqualTo(1))))
                    .andReturn().getResponse().getContentAsString();

            // JSON 에서 첫 번째 리뷰 ID 추출
            long reviewId = objectMapper.readTree(responseBody)
                    .path("content")
                    .get(0)
                    .path("reviewId")
                    .asLong();

            // when: 리뷰 수정
            UpdateReviewRequest updateRequest = new UpdateReviewRequest(
                    true, // isFavorite
                    3, // difficultyLevel
                    4, // importanceLevel
                    "// 수정된 코드", // code
                    "이 문제는 어려웠다...", // content (메모)
                    ReviewStatus.COMPLETED // status
            );

            mockMvc.perform(put("/api/reviews/" + reviewId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateRequest)))
                    .andExpect(status().isOk());

            // then: 수정된 리뷰 조회하여 변경 확인
            mockMvc.perform(get("/api/reviews/" + reviewId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.difficultyLevel", is(3)))
                    .andExpect(jsonPath("$.importanceLevel", is(4)));
        }
    }

    @Nested
    class 리뷰_필터링_시나리오 {

        @Test
        @WithMockCustomUser
        void 전체_리뷰_목록을_페이징하여_조회할_수_있다() throws Exception {
            // given: 테스트 사용자의 리뷰 생성을 위한 채점 결과 제출
            for (int i = 0; i < 3; i++) {
                CreateProblemAndJudgmentRequest request = new CreateProblemAndJudgmentRequest(
                        3000 + i, "페이징 테스트 " + i, (long) (300000 + i),
                        "user", "맞았습니다!!", 1024, 100, "Java 11", 100,
                        LocalDateTime.now(), "// code " + i);
                mockMvc.perform(post("/api/boj")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request)))
                        .andExpect(status().isCreated());
            }

            // when & then: 페이징 파라미터로 전체 리뷰 조회
            mockMvc.perform(get("/api/reviews/all?page=0&size=2"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.content", hasSize(2)));
        }
    }

    @Nested
    class 리뷰_접근_권한_시나리오 {

        @Test
        void 인증되지_않은_사용자는_리뷰를_조회할_수_없다() throws Exception {
            mockMvc.perform(get("/api/reviews"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockCustomUser
        void 다른_사용자의_리뷰에_접근할_수_없다() throws Exception {
            // given: 사용자 A가 리뷰 생성
            CreateProblemAndJudgmentRequest request = new CreateProblemAndJudgmentRequest(
                    4000, "접근 권한 테스트", 400001L,
                    "userA", "맞았습니다!!", 1024, 100, "Java 11", 100,
                    LocalDateTime.now(), "// userA code");

            mockMvc.perform(post("/api/boj")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // 사용자 A의 리뷰 ID 조회
            String responseBody = mockMvc.perform(get("/api/reviews?filter=TO_DO"))
                    .andExpect(status().isOk())
                    .andReturn().getResponse().getContentAsString();

            long reviewId = objectMapper.readTree(responseBody)
                    .path("content")
                    .get(0)
                    .path("reviewId")
                    .asLong();

            // when & then: 사용자 2번으로 요청 보내기 -> 예외 발생
            CustomUserDetails otherUser = new CustomUserDetails(2L, "pic", "userB");

            mockMvc.perform(get("/api/reviews/" + reviewId)
                            .with(user(otherUser))) //유저를 교체
                    .andExpect(status().is4xxClientError());
        }
    }
}

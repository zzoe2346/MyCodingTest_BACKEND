package com.mycodingtest.api.scenario;

import com.mycodingtest.api.collector.dto.CreateProblemAndJudgmentRequest;
import com.mycodingtest.api.scenario.support.WithMockCustomUser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * 채점 결과 수집 및 조회 시나리오 통합 테스트.
 *
 * <h3>테스트 시나리오</h3>
 * <ol>
 * <li>사용자 인증 (JWT 토큰 발급)</li>
 * <li>채점 결과 제출 (POST /api/boj)</li>
 * <li>중복 제출 확인 (GET /api/boj/check/{submissionId})</li>
 * <li>채점 결과 조회 (GET /api/judgments)</li>
 * </ol>
 */
@Transactional
@SuppressWarnings("NonAsciiCharacters")
class JudgmentCollectionScenarioTest extends ScenarioTestBase {

    @Nested
    class 채점_결과_수집_및_조회_시나리오 {

        @Test
        @WithMockCustomUser
        void 사용자가_채점_결과를_제출하고_조회할_수_있다() throws Exception {
            // given
            Long submissionId = 123456789L;
            int problemNumber = 1000;
            String problemTitle = "A+B";

            CreateProblemAndJudgmentRequest request = new CreateProblemAndJudgmentRequest(
                    problemNumber,
                    problemTitle,
                    submissionId,
                    "testUser",
                    "맞았습니다!!",
                    1024,
                    100,
                    "Java 11",
                    150,
                    LocalDateTime.now(),
                    "import java.util.*;\npublic class Main { public static void main(String[] args) { } }");

            // 1단계: 새로운 제출 ID 중복 확인 -> OK (중복 아님)
            mockMvc.perform(get("/api/boj/check/" + submissionId))
                    .andExpect(status().isOk());

            // 2단계: 채점 결과 제출 -> CREATED
            mockMvc.perform(post("/api/boj")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isCreated());

            // 3단계: 동일 제출 ID로 중복 확인 -> CONFLICT
            mockMvc.perform(get("/api/boj/check/" + submissionId)
                    .contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        @WithMockCustomUser
        void 여러_채점_결과를_제출하고_목록으로_조회할_수_있다() throws Exception {
            // given
            int problemNumber = 1001;

            // 첫 번째 제출
            CreateProblemAndJudgmentRequest request1 = new CreateProblemAndJudgmentRequest(
                    problemNumber, "A-B", 100001L,
                    "user1", "맞았습니다!!", 1024, 100, "Java 11", 150,
                    LocalDateTime.now(), "// First submission");

            // 두 번째 제출
            CreateProblemAndJudgmentRequest request2 = new CreateProblemAndJudgmentRequest(
                    problemNumber, "A-B", 100002L,
                    "user1", "틀렸습니다", 2048, 200, "Java 11", 200,
                    LocalDateTime.now(), "// Second submission");

            // when: 채점 결과 제출
            mockMvc.perform(post("/api/boj")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request1)))
                    .andExpect(status().isCreated());

            mockMvc.perform(post("/api/boj")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request2)))
                    .andExpect(status().isCreated());

            // then
            mockMvc.perform(get("/api/reviews/all"))
                    .andExpect(jsonPath("$.totalElements", is(2)));

        }
    }
}

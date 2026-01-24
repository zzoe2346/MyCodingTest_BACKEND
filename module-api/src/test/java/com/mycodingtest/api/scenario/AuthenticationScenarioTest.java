package com.mycodingtest.api.scenario;

import com.mycodingtest.api.scenario.support.WithMockCustomUser;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 인증 관련 시나리오 통합 테스트.
 *
 * <h3>테스트 시나리오</h3>
 * <ol>
 * <li>인증되지 않은 사용자의 보호된 API 접근 시도</li>
 * <li>인증된 사용자의 정보 조회</li>
 * <li>로그아웃 시 쿠키 만료 확인</li>
 * </ol>
 */
@Transactional
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationScenarioTest extends ScenarioTestBase {

    @Nested
    class 인증_상태_확인_시나리오 {

        @Test
        @WithMockCustomUser(username = "seonghun", picture = "https://image.com/test2.png")
        void 인증된_사용자는_본인_정보를_조회할_수_있다() throws Exception {
            mockMvc.perform(get("/api/me"))
                    .andExpect(jsonPath("$.name", is("seonghun")))
                    .andExpect(jsonPath("$.picture", is("https://image.com/test2.png")));
        }

        @Test
        void 인증되지_않은_사용자는_보호된_API에_접근할_수_없다() throws Exception {
            mockMvc.perform(get("/api/reviews"))
                    .andExpect(status().isUnauthorized());

            mockMvc.perform(get("/api/judgments?problemId=1"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    class 로그아웃_시나리오 {

        @Test
        @WithMockCustomUser
        void 로그아웃하면_쿠키_값이_빈_헤더가_응답에_포함된다() throws Exception {
            mockMvc.perform(get("/api/sign-out"))
                    .andExpect(status().isOk())
                    .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                    .andExpect(cookie().value("test-cookie", ""));
        }
    }

    @Nested
    class 공개_API_접근_시나리오 {

        @Test
        void 중복_확인_API는_인증_없이_접근_가능하다() throws Exception {
            mockMvc.perform(get("/api/boj/check/99999"))
                    .andExpect(status().isOk());
        }

        @Test
        void Swagger_UI는_인증_없이_접근_가능하다() throws Exception {
            mockMvc.perform(get("/swagger-ui/index.html"))
                    .andExpect(status().isOk());
        }
    }
}

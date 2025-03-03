package com.mycodingtest.judgmentresult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.judgmentresult.dto.JudgmentResultResponse;
import com.mycodingtest.judgmentresult.dto.JudgmentResultSaveRequest;
import com.mycodingtest.security.CustomUserDetails;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(JudgmentResultController.class)
class JudgmentResultControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private CookieUtil cookieUtil;
    @MockitoBean
    private JudgmentResultService judgmentResultService;

    static JudgmentResultSaveRequest request;

    @BeforeEach
    void autoSetting() {
        // 인증 완료된 유저의 요청임을 보장하고 테스트 시행
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "name");
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        TestSecurityContextHolder.setAuthentication(authToken);

        request = new JudgmentResultSaveRequest(
                1L,
                "zzoe2346",
                1023,
                "1024",
                "맞았습니다!!",
                100,
                10,
                "java",
                100,
                LocalDateTime.now()
        );
    }

    @Test
    @WithMockUser
    @DisplayName("채점 결과를 저장하면 201 상태코드를 반환한다")
    void saveJudgmentResult() throws Exception {
        // given
        doNothing().when(judgmentResultService)
                .saveJudgmentResult(any(JudgmentResultSaveRequest.class), eq(1L));

        // when then
        mockMvc.perform(post("/api/solved-problems/judgment-results")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf())
                )
                .andExpect(status().isCreated());

        verify(judgmentResultService).saveJudgmentResult(any(JudgmentResultSaveRequest.class), eq(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("채점 결과 목록을 조회하면 200 상태코드와 결과 목록을 반환한다")
    void getJudgmentResultList() throws Exception {
        // given
        List<JudgmentResultResponse> responses = List.of(
                new JudgmentResultResponse(
                        1L,
                        "zzoe2346",
                        123,
                        "틀림",
                        100,
                        100,
                        "java",
                        100,
                        LocalDateTime.now(),
                        1L)
        );

        given(judgmentResultService.getJudgmentResultList(1L, 1L))
                .willReturn(responses);

        // when then
        mockMvc.perform(get("/api/solved-problems/1/judgment-results"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].baekjoonId").value("zzoe2346"))
                .andExpect(jsonPath("$[0].resultText").value("틀림"));
    }

    @Test
    @WithMockUser
    @DisplayName("제출 ID 중복 체크시 중복되지 않으면 200 상태코드를 반환한다")
    void checkDuplicateSubmissionId() throws Exception {
        // given
        given(judgmentResultService.isSubmissionIdDuplicated(12345L))
                .willReturn(false);

        // when then
        mockMvc.perform(get("/api/solved-problems/judgment-results/submission-id-check/12345"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    @DisplayName("제출 ID가 중복되면 409 상태코드를 반환한다")
    void checkDuplicateSubmissionId_Duplicated() throws Exception {
        // given
        given(judgmentResultService.isSubmissionIdDuplicated(12345L))
                .willReturn(true);

        // when then
        mockMvc.perform(get("/api/solved-problems/judgment-results/submission-id-check/12345"))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser
    @DisplayName("코드 읽기 URL을 조회하면 200 상태코드와 URL을 반환한다")
    void getCodeReadUrl() throws Exception {
        // given
        String expectedUrl = "https://example.com/read";
        given(judgmentResultService.getCodeReadUrl("12345", 1L))
                .willReturn(expectedUrl);

        // when & then
        mockMvc.perform(get("/api/solved-problems/judgment-results/submission-code/read/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(expectedUrl));
    }

    @Test
    @WithMockUser
    @DisplayName("코드 수정 URL을 조회하면 200 상태코드와 URL을 반환한다")
    void getCodeUpdateUrl() throws Exception {
        // given
        String expectedUrl = "https://example.com/update";
        given(judgmentResultService.getCodeUpdateUrl("12345", 1L))
                .willReturn(expectedUrl);

        // when & then
        mockMvc.perform(get("/api/solved-problems/judgment-results/submission-code/update/12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.url").value(expectedUrl));
    }
}

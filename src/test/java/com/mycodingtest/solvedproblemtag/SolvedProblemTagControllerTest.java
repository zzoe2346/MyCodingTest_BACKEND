package com.mycodingtest.solvedproblemtag;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagResponse;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagSetRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@WebMvcTest(SolvedProblemTagController.class)
class SolvedProblemTagControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private CookieUtil cookieUtil;
    @MockitoBean
    private SolvedProblemTagService solvedProblemTagService;

    @BeforeEach
    void setSecurityContext() {
        CustomUserDetails customUserDetails = new CustomUserDetails(1L, "picture", "name");
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        TestSecurityContextHolder.setAuthentication(authToken);
    }

    @Test
    @WithMockUser
    @DisplayName("알고리즘 태그를 재설정한다")
    void setTags() throws Exception {
        // given
        Long solvedProblemId = 1L;
        int[] tagIds = {1, 3, 5};
        AlgorithmTagSetRequest request = new AlgorithmTagSetRequest(tagIds);

        // when then
        mockMvc.perform(put("/api/solved-problems/{solvedProblemId}/tags", solvedProblemId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(solvedProblemTagService).setAlgorithmTags(eq(solvedProblemId), any(AlgorithmTagSetRequest.class), eq(1L));
    }

    @Test
    @WithMockUser
    @DisplayName("문제의 태그를 조회한다")
    void getTags() throws Exception {
        // given
        Long solvedProblemId = 1L;
        Object[] tagIds = {1, 3, 5};
        AlgorithmTagResponse response = new AlgorithmTagResponse(tagIds);

        given(solvedProblemTagService.getAlgorithmTags(solvedProblemId)).willReturn(response);

        // when then
        mockMvc.perform(get("/api/solved-problems/{solvedProblemId}/tags", solvedProblemId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagIds[0]").value(1))
                .andExpect(jsonPath("$.tagIds[1]").value(3))
                .andExpect(jsonPath("$.tagIds[2]").value(5));
    }

    @Test
    @WithMockUser
    @DisplayName("태그 개수가 7개를 초과하면 Bad Request를 반환한다")
    void setTags_withTooManyTags() throws Exception {
        // given
        Long solvedProblemId = 1L;
        int[] tagIds = {1, 2, 3, 4, 5, 6, 7, 8}; // 8개 태그 (최대 7개)
        AlgorithmTagSetRequest request = new AlgorithmTagSetRequest(tagIds);

        // when then
        mockMvc.perform(put("/api/solved-problems/{solvedProblemId}/tags", solvedProblemId).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}

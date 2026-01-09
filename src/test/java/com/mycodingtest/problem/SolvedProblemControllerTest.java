package com.mycodingtest.problem;

import com.mycodingtest.common.util.CookieUtil;
import com.mycodingtest.common.util.JwtUtil;
import com.mycodingtest.common.security.CustomUserDetails;
import com.mycodingtest.problem.dto.SolvedProblemWithReviewResponse;
import com.mycodingtest.solvedproblemtag.dto.MyTagListResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("unit")
@WebMvcTest(SolvedProblemController.class)
class SolvedProblemControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private CookieUtil cookieUtil;
    @MockitoBean
    private JwtUtil jwtUtil;
    @MockitoBean
    private SolvedProblemService solvedProblemService;

    Long USER_ID = 1L;

    @BeforeEach
    void setSecurityContext() {
        CustomUserDetails customUserDetails = new CustomUserDetails(USER_ID, "picture", "name");
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, null);
        TestSecurityContextHolder.setAuthentication(authToken);
    }

    @Test
    @WithMockUser
    @DisplayName("푼 문제 목록을 페이지로 조회한다")
    void getSolvedProblemWithReviewPage() throws Exception {
        // given
        SolvedProblemWithReviewResponse solvedProblemWithReviewResponse = mock(SolvedProblemWithReviewResponse.class);
        given(solvedProblemWithReviewResponse.problemTitle()).willReturn("title");
        List<SolvedProblemWithReviewResponse> list = List.of(solvedProblemWithReviewResponse);
        Page<SolvedProblemWithReviewResponse> page = new PageImpl<>(list);

        given(solvedProblemService.getSolvedProblemWithRiviewPage(any(Pageable.class), eq(USER_ID)))
                .willReturn(page);

        // when then
        mockMvc.perform(get("/api/solved-problems"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].problemTitle").value("title"));
    }

    @Test
    @WithMockUser
    @DisplayName("즐겨찾기 상태를 변경한다(상태가 반전됨)")
    void changeFavorite() throws Exception {
        // when then
        mockMvc.perform(patch("/api/solved-problems/1/favorite").with(csrf()))
                .andExpect(status().isOk());

        verify(solvedProblemService).changeFavorite(any(Long.class), eq(USER_ID));
    }

    @Test
    @WithMockUser
    @DisplayName("리뷰 상태에 따른 푼 문제 목록을 조회한다")
    void getSolvedProblemByReviewStatus() throws Exception {
        // given
        SolvedProblemWithReviewResponse solvedProblemWithReviewResponse = mock(SolvedProblemWithReviewResponse.class);
        given(solvedProblemWithReviewResponse.problemTitle()).willReturn("title");
        List<SolvedProblemWithReviewResponse> list = List.of(solvedProblemWithReviewResponse);
        Page<SolvedProblemWithReviewResponse> page = new PageImpl<>(list);

        given(solvedProblemService.getSolvedProblemsByReviewedStatus(eq(true), any(Pageable.class), eq(1L)))
                .willReturn(page);

        // when then
        mockMvc.perform(get("/api/solved-problems/review/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].problemTitle").value("title"));
    }

    @Test
    @WithMockUser
    @DisplayName("즐겨찾기한 푼 문제 목록을 조회한다")
    void getFavoriteSolvedProblem() throws Exception {
        // given
        SolvedProblemWithReviewResponse solvedProblemWithReviewResponse = mock(SolvedProblemWithReviewResponse.class);
        given(solvedProblemWithReviewResponse.isFavorite()).willReturn(true);
        List<SolvedProblemWithReviewResponse> list = List.of(solvedProblemWithReviewResponse);
        Page<SolvedProblemWithReviewResponse> page = new PageImpl<>(list);

        given(solvedProblemService.getFavoriteSolvedProblem(any(Pageable.class), eq(1L)))
                .willReturn(page);

        // when then
        mockMvc.perform(get("/api/solved-problems/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].isFavorite").value(true));
    }

    @Test
    @WithMockUser
    @DisplayName("특정 태그의 푼 문제 목록을 조회한다")
    void getTaggedSolvedProblem() throws Exception {
        // given
        SolvedProblemWithReviewResponse solvedProblemWithReviewResponse = mock(SolvedProblemWithReviewResponse.class);
        given(solvedProblemWithReviewResponse.problemNumber()).willReturn(1105);
        List<SolvedProblemWithReviewResponse> list = List.of(solvedProblemWithReviewResponse);
        Page<SolvedProblemWithReviewResponse> page = new PageImpl<>(list);

        given(solvedProblemService.getTaggedSolvedProblem(eq(2L), any(Pageable.class), eq(USER_ID)))
                .willReturn(page);

        // when then
        mockMvc.perform(get("/api/solved-problems/tags/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].problemNumber").value(1105));
    }

    @Test
    @WithMockUser
    @DisplayName("내가 특정 문제에 사용한 태그 목록을 조회한다")
    void getMyTagList() throws Exception {
        // given
        Object[] tagIds = {1L, 2L, 3L};
        MyTagListResponse response = new MyTagListResponse(tagIds);

        given(solvedProblemService.getMyTagList(eq(USER_ID)))
                .willReturn(response);

        // when then
        mockMvc.perform(get("/api/solved-problems/tags"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tagIds[0]").value(1))
                .andExpect(jsonPath("$.tagIds[1]").value(2))
                .andExpect(jsonPath("$.tagIds[2]").value(3));
    }

    @Test
    @WithMockUser
    @DisplayName("푼 문제를 삭제한다")
    void deleteSolvedProblem() throws Exception {
        // when then
        mockMvc.perform(delete("/api/solved-problems/1").with(csrf()))
                .andExpect(status().isOk());

        verify(solvedProblemService).deleteSolvedProblem(eq(1L), eq(1L));
    }
}

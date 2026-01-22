package com.mycodingtest.api.judgment;

import com.mycodingtest.application.judgment.command.JudgmentCommandService;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentStatus;
import com.mycodingtest.security.CustomUserDetails;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class JudgmentQueryControllerTest {

    @Mock
    private JudgmentCommandService judgmentCommandService;

    @InjectMocks
    private JudgmentQueryController judgmentQueryController;

    @Nested
    class 채점_결과_목록_조회 {

        @Test
        void 문제ID로_채점_목록을_조회한다() {
            // given
            Long problemId = 1000L;
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");

            List<Judgment> judgments = List.of(
                    Judgment.from( problemId, userId, 12345L, JudgmentStatus.SUCCESS, Platform.BOJ, null, "code1"),
                    Judgment.from( problemId, userId, 12346L, JudgmentStatus.FAIL, Platform.BOJ, null, "code2"));
            given(judgmentCommandService.getJudgments(problemId, userId)).willReturn(judgments);

            // when
            var result = judgmentQueryController.getJudgmentResultList(problemId, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).hasSize(2);
            assertThat(result.getBody().get(0).submissionId()).isEqualTo(12345L);
            assertThat(result.getBody().get(1).submissionId()).isEqualTo(12346L);
        }

        @Test
        void 채점_결과가_없으면_빈_리스트를_반환한다() {
            // given
            Long problemId = 9999L;
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");
            given(judgmentCommandService.getJudgments(problemId, userId)).willReturn(List.of());

            // when
            var result = judgmentQueryController.getJudgmentResultList(problemId, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isEmpty();
        }
    }
}

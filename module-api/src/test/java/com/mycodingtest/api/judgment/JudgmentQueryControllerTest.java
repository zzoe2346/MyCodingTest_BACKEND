package com.mycodingtest.api.judgment;

import com.mycodingtest.application.judgment.query.JudgmentInfo;
import com.mycodingtest.application.judgment.query.JudgmentQueryService;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentStatus;
import com.mycodingtest.domain.judgment.SubmissionInfo;
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
    private JudgmentQueryService judgmentQueryService;

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

            List<JudgmentInfo> judgments = List.of(
                    JudgmentInfo.from(Judgment.from(1L, userId, SubmissionInfo.from(123L, Platform.BOJ, JudgmentStatus.SUCCESS, null, "{java}"))),
                    JudgmentInfo.from(Judgment.from(2L, userId, SubmissionInfo.from(1234L, Platform.BOJ, JudgmentStatus.FAIL, null, "{java}")))
            );
            given(judgmentQueryService.getJudgments(problemId, userId)).willReturn(judgments);

            // when
            var result = judgmentQueryController.getJudgmentResultList(problemId, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).hasSize(2);
            assertThat(result.getBody().get(0).problemId()).isEqualTo(1L);
            assertThat(result.getBody().get(0).submissionId()).isEqualTo(123L);
            assertThat(result.getBody().get(1).problemId()).isEqualTo(2L);
            assertThat(result.getBody().get(1).submissionId()).isEqualTo(1234L);
        }

        @Test
        void 채점_결과가_없으면_빈_리스트를_반환한다() {
            // given
            Long problemId = 9999L;
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");
            given(judgmentQueryService.getJudgments(problemId, userId)).willReturn(List.of());

            // when
            var result = judgmentQueryController.getJudgmentResultList(problemId, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isEmpty();
        }
    }
}

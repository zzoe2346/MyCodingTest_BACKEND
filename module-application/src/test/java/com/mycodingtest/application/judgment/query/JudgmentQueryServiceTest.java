package com.mycodingtest.application.judgment.query;

import com.mycodingtest.application.judgment.command.JudgmentCommandService;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentRepository;
import com.mycodingtest.domain.judgment.JudgmentStatus;
import com.mycodingtest.domain.judgment.SubmissionInfo;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@MockitoSettings
class JudgmentQueryServiceTest {

    @Mock
    private JudgmentRepository repository;

    @InjectMocks
    private JudgmentQueryService judgmentQueryService;

    @Nested
    class 채점_결과_조회 {

        @Test
        void 문제ID와_사용자ID로_채점_목록을_조회한다() {
            // given
            Long problemId = 1000L;
            Long userId = 1L;
            SubmissionInfo info1 = SubmissionInfo.from(123L, Platform.BOJ, JudgmentStatus.SUCCESS, null, "code1");
            SubmissionInfo info2 = SubmissionInfo.from(124L, Platform.BOJ, JudgmentStatus.FAIL, null, "code2");

            List<Judgment> expectedJudgments = List.of(
                    Judgment.from(problemId, userId, info1),
                    Judgment.from(problemId, userId, info2));
            given(repository.findByProblemIdAndUserId(problemId, userId)).willReturn(expectedJudgments);

            // when
            List<JudgmentInfo> result = judgmentQueryService.getJudgments(problemId, userId);

            // then
            assertThat(result).hasSize(2);
            verify(repository).findByProblemIdAndUserId(problemId, userId);
        }
    }

    @Nested
    class 채점_존재_확인 {

        @Test
        void 제출ID가_이미_존재하면_true를_반환한다() {
            // given
            Long submissionId = 99999L;
            Platform platform = Platform.BOJ;
            given(repository.existsBySubmissionId(submissionId, platform)).willReturn(true);

            // when
            boolean result = judgmentQueryService.isJudgmentExist(submissionId, Platform.BOJ);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 제출ID가_존재하지_않으면_false를_반환한다() {
            // given
            Long submissionId = 99999L;
            Platform platform = Platform.BOJ;
            given(repository.existsBySubmissionId(submissionId, platform)).willReturn(false);

            // when
            boolean result = judgmentQueryService.isJudgmentExist(submissionId, Platform.BOJ);

            // then
            assertThat(result).isFalse();
        }
    }

}
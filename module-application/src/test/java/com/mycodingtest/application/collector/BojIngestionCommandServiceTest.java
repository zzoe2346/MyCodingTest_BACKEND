package com.mycodingtest.application.collector;

import com.mycodingtest.application.collector.command.BojIngestionCommandService;
import com.mycodingtest.application.collector.command.CreateProblemAndJudgmentCommand;
import com.mycodingtest.application.judgment.command.CreateBojJudgmentCommand;
import com.mycodingtest.application.judgment.command.JudgmentCommandService;
import com.mycodingtest.application.judgment.query.JudgmentQueryService;
import com.mycodingtest.application.problem.command.ProblemCommandService;
import com.mycodingtest.application.problem.command.SyncProblemCommand;
import com.mycodingtest.application.review.command.CreateReviewCommand;
import com.mycodingtest.application.review.command.ReviewCommandService;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BojIngestionCommandServiceTest {

    @Mock
    private JudgmentCommandService judgmentCommandService;
    @Mock
    private JudgmentQueryService judgmentQueryService;

    @Mock
    private ProblemCommandService problemCommandService;

    @Mock
    private ReviewCommandService reviewCommandService;

    @InjectMocks
    private BojIngestionCommandService bojIngestionCommandService;

    @Nested
    class 데이터_수집 {

        @Test
        void 문제_채점_리뷰를_순서대로_생성한다() {
            // given
            CreateProblemAndJudgmentCommand command = CreateProblemAndJudgmentCommand.builder()
                    .userId(1L)
                    .problemNumber(1000)
                    .problemTitle("A+B")
                    .submissionId(12345L)
                    .baekjoonId("testUser")
                    .resultText("맞았습니다!!")
                    .memory(1024)
                    .time(100)
                    .language("Java 11")
                    .codeLength(500)
                    .submittedAt(LocalDateTime.now())
                    .sourceCode("public class Main {}")
                    .build();

            given(problemCommandService.syncProblem(any())).willReturn(1L);

            // when
            bojIngestionCommandService.ingest(command);

            // then
            verify(problemCommandService).syncProblem(any());
            verify(judgmentCommandService).createJudgmentFromBoj(any());
            verify(reviewCommandService).createReview(any());
        }
    }

    @Nested
    class 중복_제출_확인 {

        @Test
        void 중복_제출이면_예외가_발생한다() {
            // given
            Long submissionId = 99999L;
            given(judgmentQueryService.isJudgmentExist(submissionId, Platform.BOJ)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> bojIngestionCommandService.checkDuplicatedSubmissionId(submissionId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 존재하는 제출 번호입니다");
        }

        @Test
        void 중복이_아니면_예외가_발생하지_않는다() {
            // given
            Long submissionId = 99999L;
            given(judgmentQueryService.isJudgmentExist(submissionId, Platform.BOJ)).willReturn(false);

            // when & then
            bojIngestionCommandService.checkDuplicatedSubmissionId(submissionId);
            // 예외가 발생하지 않으면 성공
        }
    }
}

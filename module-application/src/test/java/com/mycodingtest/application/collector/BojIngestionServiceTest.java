package com.mycodingtest.application.collector;

import com.mycodingtest.application.collector.dto.CreateProblemAndJudgmentCommand;
import com.mycodingtest.application.judgment.JudgmentService;
import com.mycodingtest.application.problem.ProblemService;
import com.mycodingtest.application.review.ReviewService;
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
class BojIngestionServiceTest {

    @Mock
    private JudgmentService judgmentService;

    @Mock
    private ProblemService problemService;

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private BojIngestionService bojIngestionService;

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

            Problem mockProblem = Problem.from(1000, "A+B", Platform.BOJ);

            given(problemService.getOrCreateProblem(any())).willReturn(mockProblem);

            // when
            bojIngestionService.ingest(command);

            // then
            verify(problemService).getOrCreateProblem(any());
            verify(judgmentService).createJudgmentFromBoj(any());
            verify(reviewService).createReview(any());
        }
    }

    @Nested
    class 중복_제출_확인 {

        @Test
        void 중복_제출이면_예외가_발생한다() {
            // given
            Long submissionId = 99999L;
            given(judgmentService.isJudgmentExist(submissionId, Platform.BOJ)).willReturn(true);

            // when & then
            assertThatThrownBy(() -> bojIngestionService.checkDuplicatedSubmissionId(submissionId))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("이미 존재하는 제출 번호입니다");
        }

        @Test
        void 중복이_아니면_예외가_발생하지_않는다() {
            // given
            Long submissionId = 99999L;
            given(judgmentService.isJudgmentExist(submissionId, Platform.BOJ)).willReturn(false);

            // when & then
            bojIngestionService.checkDuplicatedSubmissionId(submissionId);
            // 예외가 발생하지 않으면 성공
        }
    }
}

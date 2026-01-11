package com.mycodingtest.collector.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.judgment.application.dto.RegisterBojJudgmentCommand;
import com.mycodingtest.problem.application.ProblemService;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.review.application.ReviewService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BojIngestionServiceTest {

    @InjectMocks
    private BojIngestionService bojIngestionService;

    @Mock
    private JudgmentService judgmentService;

    @Mock
    private ProblemService problemService;

    @Mock
    private ReviewService reviewService;

    @Test
    void 백준_문제_및_판정_수집_성공() {
        // given
        Long userId = 1L;
        RegisterBojSolutionCommand command = RegisterBojSolutionCommand.builder()
                .problemNumber(1000)
                .problemTitle("Title")
                .baekjoonId("bojId")
                .resultText("Success")
                .submissionId(12345L)
                .language("Java")
                .memory(100)
                .time(200)
                .codeLength(300)
                .submittedAt(LocalDateTime.now())
                .sourceCode("code")
                .build();

        Problem problem = Problem.of(1000, "Title", Platform.BOJ);
        given(problemService.getOrCreateProblemFromBoj(command.getProblemNumber(), command.getProblemTitle()))
                .willReturn(problem);

        // when
        bojIngestionService.ingest(command, userId);

        // then
        verify(problemService).getOrCreateProblemFromBoj(command.getProblemNumber(), command.getProblemTitle());
        // Updated to match the mapped command type
        verify(judgmentService).createJudgmentFromBoj(any(RegisterBojJudgmentCommand.class), eq(problem.getId()), eq(userId));
        verify(reviewService).createReview(problem.getId(), userId, command.getSourceCode(), command.getSubmittedAt(), command.getResultText());
    }

    @Test
    void 중복_제출_번호_체크_시_중복이면_예외() {
        // given
        Long submissionId = 12345L;
        given(judgmentService.isJudgmentExist(submissionId, Platform.BOJ)).willReturn(true);

        // when & then
        assertThatThrownBy(() -> bojIngestionService.checkDuplicatedSubmissionId(submissionId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 제출 번호입니다");
    }

    @Test
    void 중복_제출_번호_체크_시_중복_아니면_통과() {
        // given
        Long submissionId = 12345L;
        given(judgmentService.isJudgmentExist(submissionId, Platform.BOJ)).willReturn(false);

        // when
        bojIngestionService.checkDuplicatedSubmissionId(submissionId);

        // then
        verify(judgmentService).isJudgmentExist(submissionId, Platform.BOJ);
    }
}

package com.mycodingtest.collector.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.problem.application.ProblemService;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.review.application.ReviewService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojIngestionService {

    private final JudgmentService judgmentService;
    private final ProblemService problemService;
    private final ReviewService reviewService;

    @Transactional
    public void ingest(CreateProblemAndJudgmentFromBojCommand command, Long userId) {
        Problem problem = problemService.getOrCreateProblemFromBoj(
                command.getProblemNumber(),
                command.getProblemTitle()
        );
        judgmentService.createJudgmentFromBoj(command, problem.getId(), userId);
        reviewService.createReview(problem.getId(), userId, command.getSourceCode(), command.getSubmittedAt(), command.getResultText());
    }

    public void checkDuplicatedSubmissionId(Long submissionId) {
        boolean isDuplicated = judgmentService.isJudgmentExist(submissionId, Platform.BOJ);
        if (isDuplicated) {
            throw new IllegalStateException("이미 존재하는 제출 번호입니다: " + submissionId);
        }
    }

}

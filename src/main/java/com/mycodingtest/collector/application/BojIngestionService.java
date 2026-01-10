package com.mycodingtest.collector.application;

import com.mycodingtest.common.Platform;
import com.mycodingtest.judgment.application.JudgmentService;
import com.mycodingtest.problem.application.ProblemService;
import com.mycodingtest.problem.domain.Problem;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojIngestionService {

    private final JudgmentService judgmentService;
    private final ProblemService problemService;

    @Transactional
    public void ingest(CreateProblemAndJudgmentFromBojCommand command, Long userId) {
        Problem problem = problemService.getOrCreateProblemFromBoj(
                command.getProblemNumber(),
                command.getProblemTitle()
        );
        judgmentService.createJudgmentFromBoj(command, problem.getId(), userId);
    }

    public void checkDuplicatedSubmissionId(Long submissionId) {
        boolean isDuplicated = judgmentService.isJudgmentExist(submissionId, Platform.BOJ);
        if (isDuplicated) {
            throw new IllegalStateException("이미 존재하는 제출 번호입니다: " + submissionId);
        }
    }

}

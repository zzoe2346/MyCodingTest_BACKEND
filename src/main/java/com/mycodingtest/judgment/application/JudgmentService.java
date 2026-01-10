package com.mycodingtest.judgment.application;

import com.mycodingtest.collector.application.CreateProblemAndJudgmentFromBojCommand;
import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.judgment.domain.*;
import com.mycodingtest.judgment.dto.CreateJudgmentRequest;
import com.mycodingtest.judgment.dto.JudgmentResultMapper;
import com.mycodingtest.judgment.dto.JudgmentResultResponse;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.query.dashboard.DashBoardQuearyRepository;
import com.mycodingtest.review.domain.Review;
import com.mycodingtest.storage.StorageService;
import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JudgmentService {

    private final JudgmentRepository judgmentResultRepository;
    private final DashBoardQuearyRepository solvedDashBoardQuearyRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    @Transactional
    public void createJudgment(CreateJudgmentRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotOurUserException::new);

        Problem problem = prepareSolvedProblem(request, user);

        judgmentResultRepository.save(new Submission(
                request.baekjoonId(),
                request.codeLength(),
                request.language(),
                request.memory(),
                request.problemNumber(),
                request.resultText(),
                request.submissionId(),
                request.submittedAt(),
                request.time(),
                user,
                problem)
        );
    }

    private Problem prepareSolvedProblem(CreateJudgmentRequest request, User user) {
        Problem problem = solvedDashBoardQuearyRepository.findByUserIdAndProblemNumber(user.getId(), request.problemNumber())
                .orElseGet(() -> new Problem(request.problemNumber(), request.problemTitle(), user, new Review(user)));
        problem.updateRecentResult(request.submittedAt(), request.resultText());
        return solvedDashBoardQuearyRepository.save(problem);
    }

    @Transactional(readOnly = true)
    public List<JudgmentResultResponse> getJudgmentResultList(Long solvedProblemId, Long userId) {
        List<Submission> resultList = judgmentResultRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblemId, userId);

        return resultList.stream()
                .map(JudgmentResultMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isJudgmentExist(Long submissionId, Platform boj) {
        return judgmentResultRepository.existsBySubmissionId(submissionId);
    }

    public String getCodeReadUrl(String submissionId, Long userId) {
        return storageService.getCodeReadUrl(submissionId, userId);
    }

    public String getCodeUpdateUrl(String submissionId, Long userId) {
        return storageService.getCodeUpdateUrl(submissionId, userId);
    }

    public void createJudgmentFromBoj(CreateProblemAndJudgmentFromBojCommand command, Long problemId, Long userId) {
        MetaData metaData = new BojMetaData(
                command.getSubmissionId(),
                command.getBaekjoonId(),
                command.getResultText(),
                command.getMemory(),
                command.getTime(),
                command.getLanguage(),
                command.getCodeLength(),
                command.getSubmittedAt()
        );

        judgmentResultRepository.save(
                Judgment.of(
                        problemId,
                        userId,
                        command.getSubmissionId(),
                        JudgmentStatus.SUCCESS,
                        Platform.BOJ,
                        metaData)
        );
    }

}

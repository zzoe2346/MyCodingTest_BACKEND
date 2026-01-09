package com.mycodingtest.judgmentresult;

import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.judgmentresult.dto.JudgmentResultResponse;
import com.mycodingtest.judgmentresult.dto.JudgmentResultSaveRequest;
import com.mycodingtest.review.domain.Review;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.problem.domain.ProblemRepository;
import com.mycodingtest.storage.StorageService;
import com.mycodingtest.user.domain.User;
import com.mycodingtest.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JudgmentResultService {

    private final JudgmentResultRepository judgmentResultRepository;
    private final ProblemRepository solvedProblemRepository;
    private final UserRepository userRepository;
    private final StorageService storageService;

    public JudgmentResultService(JudgmentResultRepository judgmentResultRepository, ProblemRepository solvedProblemRepository, UserRepository userRepository, StorageService storageService) {
        this.solvedProblemRepository = solvedProblemRepository;
        this.judgmentResultRepository = judgmentResultRepository;
        this.userRepository = userRepository;
        this.storageService = storageService;
    }

    @Transactional
    public void saveJudgmentResult(JudgmentResultSaveRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotOurUserException::new);

        Problem problem = prepareSolvedProblem(request, user);

        judgmentResultRepository.save(new JudgmentResult(
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

    private Problem prepareSolvedProblem(JudgmentResultSaveRequest request, User user) {
        Problem problem = solvedProblemRepository.findByUserIdAndProblemNumber(user.getId(), request.problemNumber())
                .orElseGet(() -> new Problem(request.problemNumber(), request.problemTitle(), user, new Review(user)));
        problem.updateRecentResult(request.submittedAt(), request.resultText());
        return solvedProblemRepository.save(problem);
    }

    @Transactional(readOnly = true)
    public List<JudgmentResultResponse> getJudgmentResultList(Long solvedProblemId, Long userId) {
        List<JudgmentResult> resultList = judgmentResultRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblemId, userId);

        return resultList.stream()
                .map(JudgmentResultMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isSubmissionIdDuplicated(Long submissionId) {
        return judgmentResultRepository.existsBySubmissionId(submissionId);
    }

    public String getCodeReadUrl(String submissionId, Long userId) {
        return storageService.getCodeReadUrl(submissionId, userId);
    }

    public String getCodeUpdateUrl(String submissionId, Long userId) {
        return storageService.getCodeUpdateUrl(submissionId, userId);
    }
}

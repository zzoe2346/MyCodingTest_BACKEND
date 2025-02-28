package com.mycodingtest.judgmentresult;

import com.mycodingtest.judgmentresult.dto.JudgmentResultResponse;
import com.mycodingtest.judgmentresult.dto.JudgmentResultSaveRequest;
import com.mycodingtest.review.Review;
import com.mycodingtest.solvedproblem.SolvedProblem;
import com.mycodingtest.user.User;
import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.solvedproblem.SolvedProblemRepository;
import com.mycodingtest.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class JudgmentResultService {

    private final JudgmentResultRepository judgmentResultRepository;
    private final SolvedProblemRepository solvedProblemRepository;
    private final UserRepository userRepository;

    public JudgmentResultService(JudgmentResultRepository judgmentResultRepository, SolvedProblemRepository solvedProblemRepository, UserRepository userRepository) {
        this.solvedProblemRepository = solvedProblemRepository;
        this.judgmentResultRepository = judgmentResultRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveJudgmentResult(JudgmentResultSaveRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(NotOurUserException::new);

        SolvedProblem solvedProblem = prepareSolvedProblem(request, user);

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
                solvedProblem)
        );
    }

    private SolvedProblem prepareSolvedProblem(JudgmentResultSaveRequest request, User user) {
        SolvedProblem solvedProblem = solvedProblemRepository.findByUserIdAndProblemNumber(user.getId(), request.problemNumber())
                .orElseGet(() -> new SolvedProblem(request.problemNumber(), request.problemTitle(), user, new Review(user)));
        solvedProblem.updateRecentResult(request.submittedAt(), request.resultText());
        return solvedProblemRepository.save(solvedProblem);
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
}

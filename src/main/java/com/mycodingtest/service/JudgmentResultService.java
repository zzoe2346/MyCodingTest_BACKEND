package com.mycodingtest.service;

import com.mycodingtest.dto.JudgmentResultResponse;
import com.mycodingtest.dto.JudgmentResultSaveRequest;
import com.mycodingtest.entity.JudgmentResult;
import com.mycodingtest.entity.Review;
import com.mycodingtest.entity.SolvedProblem;
import com.mycodingtest.entity.User;
import com.mycodingtest.exception.NotOurUserException;
import com.mycodingtest.repository.JudgmentResultRepository;
import com.mycodingtest.repository.SolvedProblemRepository;
import com.mycodingtest.repository.UserRepository;
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
        solvedProblem.setRecentSubmitAt(request.submittedAt());
        solvedProblem.setRecentResultText(request.resultText());
        return solvedProblemRepository.save(solvedProblem);
    }

    @Transactional(readOnly = true)
    public List<JudgmentResultResponse> getJudgmentResultList(Long solvedProblemId, Long userId) {
        List<JudgmentResult> resultList = judgmentResultRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblemId, userId);

        if (!resultList.isEmpty() && !resultList.getFirst().getUser().getId().equals(userId)) {
            throw new InvalidOwnershipException();
        }

        return resultList.stream()
                .map(judgmentResult -> {
                    return new JudgmentResultResponse(judgmentResult.getSubmissionId(), judgmentResult.getBaekjoonId(), judgmentResult.getProblemId(), judgmentResult.getResultText(), judgmentResult.getMemory(), judgmentResult.getTime(), judgmentResult.getLanguage(), judgmentResult.getCodeLength(), judgmentResult.getSubmittedAt(), judgmentResult.getId());
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public boolean isSubmissionIdDuplicated(Long submissionId) {
        return judgmentResultRepository.existsBySubmissionId(submissionId);
    }
}

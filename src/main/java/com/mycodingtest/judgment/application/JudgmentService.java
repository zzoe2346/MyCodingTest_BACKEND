package com.mycodingtest.judgment.application;

import com.mycodingtest.collector.application.CreateProblemAndJudgmentFromBojCommand;
import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.judgment.domain.BojMetaData;
import com.mycodingtest.judgment.domain.Judgment;
import com.mycodingtest.judgment.domain.JudgmentRepository;
import com.mycodingtest.judgment.domain.JudgmentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JudgmentService {

    private final JudgmentRepository judgmentRepository;

//    @Transactional *** 과거에 하던 방식. 필요할 수 도 있으니 아직 삭제는 않는다.
//    public void createJudgment(CreateJudgmentRequest request, Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(NotOurUserException::new);
//
//        Problem problem = prepareSolvedProblem(request, user);
//
//        judgmentRepository.save(new Judgment(
//                request.baekjoonId(),
//                request.codeLength(),
//                request.language(),
//                request.memory(),
//                request.problemNumber(),
//                request.resultText(),
//                request.submissionId(),
//                request.submittedAt(),
//                request.time(),
//                user,
//                problem)
//        );
//    }
//
//    private Problem prepareSolvedProblem(CreateJudgmentRequest request, User user) {
//        Problem problem = solvedDashBoardQuearyRepository.findByUserIdAndProblemNumber(user.getId(), request.problemNumber())
//                .orElseGet(() -> new Problem(request.problemNumber(), request.problemTitle(), user, new Review(user)));
//        problem.updateRecentResult(request.submittedAt(), request.resultText());
//        return solvedDashBoardQuearyRepository.save(problem);
//    }

    @Transactional(readOnly = true)
    public List<Judgment> readJudgments(Long problemId, Long userId) {
//        List<Judgment> judgments = judgmentRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblemId, userId);
        return judgmentRepository.findByProblemIdAndUserId(problemId, userId);
    }

    @Transactional(readOnly = true)
    public boolean isJudgmentExist(Long submissionId, Platform boj) {
        return judgmentRepository.existsBySubmissionId(submissionId);//TODO: 플랫폼 조건 빠짐
    }

    @Transactional
    public Judgment createJudgmentFromBoj(CreateProblemAndJudgmentFromBojCommand command, Long problemId, Long userId) {
        return judgmentRepository.save(
                Judgment.of(
                        problemId,
                        userId,
                        command.getSubmissionId(),
                        JudgmentStatus.SUCCESS,
                        Platform.BOJ,
                        new BojMetaData(
                                command.getSubmissionId(),
                                command.getBaekjoonId(),
                                command.getResultText(),
                                command.getMemory(),
                                command.getTime(),
                                command.getLanguage(),
                                command.getCodeLength(),
                                command.getSubmittedAt()
                        )
                ));
    }

    @Transactional
    public void deleteJudgment(Long judgmentId, Long userId) {
        judgmentRepository.deleteByIdAndUserId(judgmentId, userId);
    }
}

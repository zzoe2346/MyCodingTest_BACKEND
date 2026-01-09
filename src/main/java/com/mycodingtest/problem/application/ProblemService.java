package com.mycodingtest.problem.application;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.judgmentresult.JudgmentResult;
import com.mycodingtest.judgmentresult.JudgmentResultRepository;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.problem.domain.ProblemRepository;
import com.mycodingtest.problem.dto.ProblemWithReviewResponse;
import com.mycodingtest.solvedproblemtag.dto.MyTagListResponse;
import com.mycodingtest.storage.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final JudgmentResultRepository judgmentResultRepository;
    private final StorageService storageService;

    @Transactional(readOnly = true)
    public Page<ProblemWithReviewResponse> getSolvedProblemWithRiviewPage(Pageable pageable, Long userId) {
        return problemRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    public void changeFavorite(Long solvedProblemId, Long userId) {
        Problem problem = getSolvedProblemAndValidateOwnership(solvedProblemId, userId);

        problem.reverseFavoriteStatus();
    }

    @Transactional(readOnly = true)
    public Page<ProblemWithReviewResponse> getSolvedProblemsByReviewedStatus(boolean isReviewed, Pageable pageable, Long userId) {
        return problemRepository.findAllByUserIdAndReviewed(userId, isReviewed, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProblemWithReviewResponse> getFavoriteSolvedProblem(Pageable pageable, Long userId) {
        return problemRepository.findAllByUserIdAndFavoriteIsTrue(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProblemWithReviewResponse> getTaggedSolvedProblem(Long tagId, Pageable pageable, Long userId) {
        return problemRepository.findByAlgorithmTagAndUserId(tagId, userId, pageable);
    }

    @Transactional(readOnly = true)
    public MyTagListResponse getMyTagList(Long userId) {
        return new MyTagListResponse(problemRepository.findDistinctTagIdsByUserId(userId).toArray());
    }

    @Transactional
    public void deleteSolvedProblem(Long solvedProblemId, Long userId) {
        Problem problem = getSolvedProblemAndValidateOwnership(solvedProblemId, userId);

        List<JudgmentResult> judgmentResults = judgmentResultRepository.findAllBySolvedProblem(problem);
        List<String> submissionIdList = judgmentResults.stream()
                .map(judgmentResult -> String.valueOf(judgmentResult.getSubmissionId()))
                .toList();

        storageService.deleteMemo(String.valueOf(problem.getReview().getId()), userId);
        storageService.deleteCodes(submissionIdList, userId);

        problemRepository.delete(problem);
        judgmentResultRepository.deleteAll(judgmentResults);
    }

    private Problem getSolvedProblemAndValidateOwnership(Long solvedProblemId, Long userId) {
        Problem problem = problemRepository.findById(solvedProblemId)
                .orElseThrow(ResourceNotFoundException::new);

        problem.validateOwnership(userId);

        return problem;
    }

}

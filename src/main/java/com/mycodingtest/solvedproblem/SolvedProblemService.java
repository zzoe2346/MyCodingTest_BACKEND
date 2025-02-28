package com.mycodingtest.solvedproblem;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.judgmentresult.JudgmentResult;
import com.mycodingtest.judgmentresult.JudgmentResultRepository;
import com.mycodingtest.solvedproblem.dto.SolvedProblemWithReviewResponse;
import com.mycodingtest.tag.MyTagListResponse;
import com.mycodingtest.storage.StorageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolvedProblemService {

    private final SolvedProblemRepository solvedProblemRepository;
    private final JudgmentResultRepository judgmentResultRepository;
    private final StorageService storageService;

    public SolvedProblemService(SolvedProblemRepository solvedProblemRepository, JudgmentResultRepository judgmentResultRepository, StorageService storageService) {
        this.solvedProblemRepository = solvedProblemRepository;
        this.judgmentResultRepository = judgmentResultRepository;
        this.storageService = storageService;
    }

    @Transactional(readOnly = true)
    public Page<SolvedProblemWithReviewResponse> getSolvedProblemWithRiviewPage(Pageable pageable, Long userId) {
        return solvedProblemRepository.findAllByUserId(userId, pageable);
    }

    @Transactional
    public void changeFavorite(Long solvedProblemId, Long userId) {
        SolvedProblem solvedProblem = getSolvedProblemAndValidateOwnership(solvedProblemId, userId);

        solvedProblem.reverseFavoriteStatus();
    }

    @Transactional(readOnly = true)
    public Page<SolvedProblemWithReviewResponse> getSolvedProblemsByReviewedStatus(boolean isReviewed, Pageable pageable, Long userId) {
        return solvedProblemRepository.findAllByUserIdAndReviewed(userId, isReviewed, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SolvedProblemWithReviewResponse> getFavoriteSolvedProblem(Pageable pageable, Long userId) {
        return solvedProblemRepository.findAllByUserIdAndFavoriteIsTrue(userId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<SolvedProblemWithReviewResponse> getTaggedSolvedProblem(Long tagId, Pageable pageable, Long userId) {
        return solvedProblemRepository.findByAlgorithmTagAndUserId(tagId, userId, pageable);
    }

    @Transactional(readOnly = true)
    public MyTagListResponse getMyTagList(Long userId) {
        return new MyTagListResponse(solvedProblemRepository.findDistinctTagIdsByUserId(userId).toArray());
    }

    @Transactional
    public void deleteSolvedProblem(Long solvedProblemId, Long userId) {
        SolvedProblem solvedProblem = getSolvedProblemAndValidateOwnership(solvedProblemId, userId);

        List<JudgmentResult> judgmentResults = judgmentResultRepository.findAllBySolvedProblem(solvedProblem);
        List<String> submissionIdList = judgmentResults.stream()
                .map(judgmentResult -> String.valueOf(judgmentResult.getSubmissionId()))
                .toList();

        storageService.deleteMemo(String.valueOf(solvedProblem.getReview().getId()), userId);
        storageService.deleteCodes(submissionIdList, userId);

        solvedProblemRepository.delete(solvedProblem);
        judgmentResultRepository.deleteAll(judgmentResults);
    }

    private SolvedProblem getSolvedProblemAndValidateOwnership(Long solvedProblemId, Long userId) {
        SolvedProblem solvedProblem = solvedProblemRepository.findById(solvedProblemId)
                .orElseThrow(ResourceNotFoundException::new);

        solvedProblem.validateOwnership(userId);

        return solvedProblem;
    }
}

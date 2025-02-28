package com.mycodingtest.solvedproblem;

import com.mycodingtest.storage.S3Service;
import com.mycodingtest.judgmentresult.JudgmentResult;
import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.judgmentresult.JudgmentResultRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SolvedProblemService {

    private final SolvedProblemRepository solvedProblemRepository;
    private final JudgmentResultRepository judgmentResultRepository;
    private final S3Service s3Service;

    public SolvedProblemService(SolvedProblemRepository solvedProblemRepository, JudgmentResultRepository judgmentResultRepository, S3Service s3Service) {
        this.solvedProblemRepository = solvedProblemRepository;
        this.judgmentResultRepository = judgmentResultRepository;
        this.s3Service = s3Service;
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

        s3Service.deleteMemo(String.valueOf(solvedProblem.getReview().getId()), userId);
        s3Service.deleteCodes(submissionIdList, userId);

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

package com.mycodingtest.service;

import com.mycodingtest.dto.MyTagListResponse;
import com.mycodingtest.dto.SolvedProblemWithReviewResponse;
import com.mycodingtest.entity.JudgmentResult;
import com.mycodingtest.entity.SolvedProblem;
import com.mycodingtest.exception.InvalidOwnershipException;
import com.mycodingtest.exception.ResourceNotFoundException;
import com.mycodingtest.repository.JudgmentResultRepository;
import com.mycodingtest.repository.SolvedProblemRepository;
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
        SolvedProblem solvedProblem = solvedProblemRepository.findById(solvedProblemId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!solvedProblem.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

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
        SolvedProblem solvedProblem = solvedProblemRepository.findById(solvedProblemId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!solvedProblem.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        List<JudgmentResult> judgmentResults = judgmentResultRepository.findAllBySolvedProblem(solvedProblem);
        List<String> submissionIdList = judgmentResults.stream()
                .map(judgmentResult -> String.valueOf(judgmentResult.getSubmissionId()))
                .toList();

        s3Service.deleteMemo(String.valueOf(solvedProblem.getReview().getId()), userId);
        s3Service.deleteCodes(submissionIdList, userId);

        solvedProblemRepository.delete(solvedProblem);
        judgmentResultRepository.deleteAll(judgmentResults);
    }
}

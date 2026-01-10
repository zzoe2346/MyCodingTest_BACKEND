package com.mycodingtest.review.application;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.review.domain.Review;
import com.mycodingtest.review.domain.ReviewRepository;
import com.mycodingtest.review.dto.*;
import com.mycodingtest.storage.StorageService;
import com.mycodingtest.storage.dto.UrlResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StorageService storageService;

    @Transactional
    public void updateReviewRatingLevels(ReviewRatingLevelsUpdateRequest request, Long reviewId, Long userId) {
        Review review = getReviewAndValidateOwnership(reviewId, userId);
        review.updateRatingLevels(request.difficultyLevel(), request.importanceLevel());
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId, Long userId) {
        Review review = getReviewAndValidateOwnership(reviewId, userId);
        return ReviewMapper.toResponse(review);
    }

    @Transactional(readOnly = true)
    public UrlResponse getMemoUpdateUrl(Long reviewId, Long userId) {
        getReviewAndValidateOwnership(reviewId, userId);

        return new UrlResponse(storageService.getMemoUpdateUrl(String.valueOf(reviewId), userId));
    }

    @Transactional(readOnly = true)
    public UrlResponse getMemoReadUrl(Long reviewId, Long userId) {
        getReviewAndValidateOwnership(reviewId, userId);

        return new UrlResponse(storageService.getMemoReadUrl(String.valueOf(reviewId), userId));
    }

    @Transactional
    public ReviewRecentStatusResponse updateReviewStatus(Long reviewId, Long userId) {
        Review review = getReviewAndValidateOwnership(reviewId, userId);

        review.completeReview();

        return ReviewMapper.toRecentStatusResponse(review);
    }

    @Transactional(readOnly = true)
    public WaitReviewCountResponse getWaitReviewCount(Long userId) {
        return new WaitReviewCountResponse(reviewRepository.countAllByReviewedIsFalseAndUserId(userId));
    }

    private Review getReviewAndValidateOwnership(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);

        review.validateOwnership(userId);

        return review;
    }

    public void createReview(Long problemId, Long userId, String sourceCode) {
        reviewRepository.save(new Review(problemId, userId, sourceCode));
    }

    //이사
//
//    @Transactional
//    public void changeFavorite(Long solvedProblemId, Long userId) {
//        Problem problem = getSolvedProblemAndValidateOwnership(solvedProblemId, userId);
//        problem.reverseFavoriteStatus();
//    }

}

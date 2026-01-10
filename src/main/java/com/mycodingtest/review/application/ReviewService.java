package com.mycodingtest.review.application;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.review.api.dto.*;
import com.mycodingtest.review.domain.Review;
import com.mycodingtest.review.domain.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

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

    public void createReview(Long problemId, Long userId, String sourceCode, LocalDateTime submittedAt, String resultText) {
        reviewRepository.save(new Review(problemId, userId, sourceCode,submittedAt, resultText));
    }

    public void changeFavorite(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow();
        review.validateOwnership(userId);
        review.changeFavorite();
        reviewRepository.save(review);
    }

}

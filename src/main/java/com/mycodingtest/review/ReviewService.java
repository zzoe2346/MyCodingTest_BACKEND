package com.mycodingtest.review;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.review.dto.ReviewRecentStatusResponse;
import com.mycodingtest.review.dto.ReviewResponse;
import com.mycodingtest.review.dto.WaitReviewCountResponse;
import com.mycodingtest.storage.StorageService;
import com.mycodingtest.storage.dto.UrlResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final StorageService storageService;

    public ReviewService(ReviewRepository reviewRepository, StorageService storageService) {
        this.reviewRepository = reviewRepository;
        this.storageService = storageService;
    }

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
}

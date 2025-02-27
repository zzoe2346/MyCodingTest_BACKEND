package com.mycodingtest.service;

import com.mycodingtest.dto.*;
import com.mycodingtest.entity.Review;
import com.mycodingtest.exception.ResourceNotFoundException;
import com.mycodingtest.mapper.ReviewMapper;
import com.mycodingtest.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    public ReviewService(ReviewRepository reviewRepository, S3Service s3Service) {
        this.reviewRepository = reviewRepository;
        this.s3Service = s3Service;
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

        return new UrlResponse(s3Service.getMemoUpdateUrl(String.valueOf(reviewId), userId));
    }

    @Transactional(readOnly = true)
    public UrlResponse getMemoReadUrl(Long reviewId, Long userId) {
        getReviewAndValidateOwnership(reviewId, userId);

        return new UrlResponse(s3Service.getMemoReadUrl(String.valueOf(reviewId), userId));
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

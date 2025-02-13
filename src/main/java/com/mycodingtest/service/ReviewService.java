package com.mycodingtest.service;

import com.mycodingtest.dto.*;
import com.mycodingtest.entity.Review;
import com.mycodingtest.exception.InvalidOwnershipException;
import com.mycodingtest.exception.ResourceNotFoundException;
import com.mycodingtest.repository.ReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!review.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        review.setDifficultyLevel(request.difficultyLevel());
        review.setImportanceLevel(request.importanceLevel());
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!review.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        return new ReviewResponse(review.getDifficultyLevel(), review.getImportanceLevel(), review.isReviewed(), review.getReviewedAt());
    }

    @Transactional(readOnly = true)
    public UrlResponse getMemoUpdateUrl(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!review.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        return new UrlResponse(s3Service.getMemoUpdateUrl(String.valueOf(reviewId), userId));
    }

    @Transactional(readOnly = true)
    public UrlResponse getMemoReadUrl(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!review.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        return new UrlResponse(s3Service.getMemoReadUrl(String.valueOf(reviewId), userId));
    }

    @Transactional
    public ReviewRecentStatusResponse updateReviewStatus(Long reviewId, ReviewStatusUpdateRequest request, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!review.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        review.setReviewed(request.reviewed());
        if (request.reviewed()) review.setReviewedAt(LocalDateTime.now());
        return new ReviewRecentStatusResponse(review.isReviewed(), review.getReviewedAt());
    }

    @Transactional(readOnly = true)
    public WaitReviewCountResponse getWaitReviewCount(Long userId) {
        return new WaitReviewCountResponse(reviewRepository.countAllByReviewedIsFalseAndUserId(userId));
    }
}

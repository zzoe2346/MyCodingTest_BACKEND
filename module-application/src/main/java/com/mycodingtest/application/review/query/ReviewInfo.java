package com.mycodingtest.application.review.query;

import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewInfo(
        Long id,
        Long userId,
        Long problemId,
        Boolean isFavorite,
        Integer difficultyLevel,
        Integer importanceLevel,
        String revisedCode,
        String content,
        ReviewStatus status,
        LocalDateTime reviewedAt,
        boolean reviewed
) {
    public static ReviewInfo from(Review review) {
        return new ReviewInfo(
                review.getId(),
                review.getUserId(),
                review.getProblemId(),
                review.isFavorited(),
                review.getDifficultyLevel(),
                review.getImportanceLevel(),
                review.getRevisedCode(),
                review.getContent(),
                review.getStatus(),
                review.getReviewedAt(),
                review.getReviewed()
        );
    }
}

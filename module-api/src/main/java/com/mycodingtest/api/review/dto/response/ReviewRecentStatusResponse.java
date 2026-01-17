package com.mycodingtest.api.review.dto.response;

import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewStatus;

public record ReviewRecentStatusResponse(
        Boolean isFavorite,
        Integer difficultyLevel,
        Integer importanceLevel,
        String code,
        String content,
        ReviewStatus status
) {
    public static ReviewRecentStatusResponse from(Review updatedReview) {
        return new ReviewRecentStatusResponse(
                updatedReview.isFavorited(),
                updatedReview.getDifficultyLevel(),
                updatedReview.getImportanceLevel(),
                updatedReview.getRevisedCode(),
                updatedReview.getContent(),
                updatedReview.getStatus()
        );
    }
}

package com.mycodingtest.api.review.dto.response;

import com.mycodingtest.application.review.command.UpdateReviewResult;
import com.mycodingtest.domain.review.ReviewStatus;

public record ReviewRecentStatusResponse(
        Boolean isFavorite,
        Integer difficultyLevel,
        Integer importanceLevel,
        String code,
        String content,
        ReviewStatus status
) {
    public static ReviewRecentStatusResponse from(UpdateReviewResult updatedReview) {
        return new ReviewRecentStatusResponse(
                updatedReview.isFavorited(),
                updatedReview.difficultyLevel(),
                updatedReview.importanceLevel(),
                updatedReview.revisedCode(),
                updatedReview.content(),
                updatedReview.status()
        );
    }
}

package com.mycodingtest.api.review.dto.response;

import com.mycodingtest.application.review.command.UpdateReviewResult;
import com.mycodingtest.domain.review.ReviewStatus;

public record UpdatedReviewResponse(
        Boolean isFavorite,
        Integer difficultyLevel,
        Integer importanceLevel,
        String code,
        String content,
        ReviewStatus status
) {
    public static UpdatedReviewResponse from(UpdateReviewResult updatedReview) {
        return new UpdatedReviewResponse(
                updatedReview.isFavorited(),
                updatedReview.difficultyLevel(),
                updatedReview.importanceLevel(),
                updatedReview.revisedCode(),
                updatedReview.content(),
                updatedReview.status()
        );
    }
}

package com.mycodingtest.api.review.dto.request;

import com.mycodingtest.application.review.dto.UpdateReviewCommand;
import com.mycodingtest.domain.review.ReviewStatus;

public record UpdateReviewRequest(
        Boolean isFavorite,
        Integer difficultyLevel,
        Integer importanceLevel,
        String code,
        String content,
        ReviewStatus status
) {
    public UpdateReviewCommand toCommand(Long reviewId, Long userId) {
        return new UpdateReviewCommand(reviewId, userId, isFavorite, difficultyLevel, importanceLevel, code, content, status);
    }
}

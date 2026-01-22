package com.mycodingtest.application.review.command;

import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewStatus;

public record UpdateReviewResult(
        Boolean isFavorited,
        Integer difficultyLevel,
        Integer importanceLevel,
        String revisedCode,
        String content,
        ReviewStatus status
) {
    public static UpdateReviewResult from(Review review) {
        return new UpdateReviewResult(
                review.isFavorited(),
                review.getDifficultyLevel(),
                review.getImportanceLevel(),
                review.getRevisedCode(),
                review.getContent(),
                review.getStatus());
    }
}

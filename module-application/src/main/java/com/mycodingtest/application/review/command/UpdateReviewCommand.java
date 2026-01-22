package com.mycodingtest.application.review.command;

import com.mycodingtest.domain.review.ReviewStatus;

public record UpdateReviewCommand(
        Long reviewId,
        Long userId,
        Boolean isFavorite,
        Integer difficultyLevel,
        Integer importanceLevel,
        String code,
        String content,
        ReviewStatus status
) {
    public static UpdateReviewCommand from(
            Long reviewId,
            Long userId,
            Boolean isFavorite,
            Integer difficultyLevel,
            Integer importanceLevel,
            String code,
            String content,
            ReviewStatus status
    ) {
        return new UpdateReviewCommand(
                reviewId,
                userId,
                isFavorite,
                difficultyLevel,
                importanceLevel,
                code,
                content,
                status
        );
    }
}

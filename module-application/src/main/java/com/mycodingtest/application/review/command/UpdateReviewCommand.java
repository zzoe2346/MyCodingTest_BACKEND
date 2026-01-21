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
}

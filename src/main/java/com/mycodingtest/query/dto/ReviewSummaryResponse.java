package com.mycodingtest.query.dto;

import com.mycodingtest.review.domain.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewSummaryResponse(
        // Problem 정보
        Long solvedProblemId,
        int problemNumber,
        String problemTitle,
        // Review 정보
        LocalDateTime recentSubmitAt,
        String recentResultText,
        boolean isFavorite,
        Long reviewId,
        int difficultyLevel,
        int importanceLevel,
        boolean isReviewed,
        LocalDateTime reviewedAt
) {
}


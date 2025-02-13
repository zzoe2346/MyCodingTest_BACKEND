package com.mycodingtest.dto;

import java.time.LocalDateTime;

public record SolvedProblemWithReviewResponse(
        Long solvedProblemId,
        int problemNumber,
        String problemTitle,
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


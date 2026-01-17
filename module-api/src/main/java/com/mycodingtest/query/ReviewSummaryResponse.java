package com.mycodingtest.query;


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
        boolean isReviewed,
        LocalDateTime reviewedAt
) {
}


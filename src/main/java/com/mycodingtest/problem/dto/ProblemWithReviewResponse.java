package com.mycodingtest.problem.dto;

import java.time.LocalDateTime;

public record ProblemWithReviewResponse(
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


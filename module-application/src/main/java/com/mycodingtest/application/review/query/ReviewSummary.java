package com.mycodingtest.application.review.query;

import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.review.Review;

import java.time.LocalDateTime;

public record ReviewSummary(
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
    public static ReviewSummary from(Problem problem, Review review) {
        return new ReviewSummary(
                problem.getId(),
                problem.getProblemNumber(),
                problem.getProblemTitle(),
                review.getRecentSubmitAt(),
                review.getRecentResult(),
                review.isFavorited(),
                review.getId(),
                review.getDifficultyLevel(),
                review.getImportanceLevel(),
                review.getReviewed(),
                review.getReviewedAt()
        );
    }
}

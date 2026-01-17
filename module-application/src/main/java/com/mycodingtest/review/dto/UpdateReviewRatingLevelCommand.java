package com.mycodingtest.review.dto;

public record UpdateReviewRatingLevelCommand(
        int difficultyLevel,
        int importanceLevel
) {
}

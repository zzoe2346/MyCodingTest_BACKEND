package com.mycodingtest.application.review.dto;

public record UpdateReviewRatingLevelCommand(
        int difficultyLevel,
        int importanceLevel
) {
}

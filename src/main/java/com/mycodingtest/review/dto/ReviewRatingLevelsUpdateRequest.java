package com.mycodingtest.review.dto;

public record ReviewRatingLevelsUpdateRequest(
        int difficultyLevel,
        int importanceLevel
) {
}

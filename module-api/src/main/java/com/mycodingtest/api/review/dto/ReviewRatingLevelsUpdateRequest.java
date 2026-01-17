package com.mycodingtest.api.review.dto;

public record ReviewRatingLevelsUpdateRequest(
        int difficultyLevel,
        int importanceLevel
) {
}

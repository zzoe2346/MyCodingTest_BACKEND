package com.mycodingtest.review.api.dto;

public record ReviewRatingLevelsUpdateRequest(
        int difficultyLevel,
        int importanceLevel
) {
}

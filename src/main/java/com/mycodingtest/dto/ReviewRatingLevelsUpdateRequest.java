package com.mycodingtest.dto;

public record ReviewRatingLevelsUpdateRequest(
        int difficultyLevel,
        int importanceLevel
) {
}

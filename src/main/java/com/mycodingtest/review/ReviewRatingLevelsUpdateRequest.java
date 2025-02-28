package com.mycodingtest.review;

public record ReviewRatingLevelsUpdateRequest(
        int difficultyLevel,
        int importanceLevel
) {
}

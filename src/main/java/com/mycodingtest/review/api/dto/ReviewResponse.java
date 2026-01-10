package com.mycodingtest.review.api.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        int difficultyLevel,
        int importanceLevel,
        boolean reviewed,
        LocalDateTime reviewedAt) {
}

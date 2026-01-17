package com.mycodingtest.review.dto;

import java.time.LocalDateTime;

public record ReviewResponse(
        int difficultyLevel,
        int importanceLevel,
        boolean reviewed,
        LocalDateTime reviewedAt) {
}

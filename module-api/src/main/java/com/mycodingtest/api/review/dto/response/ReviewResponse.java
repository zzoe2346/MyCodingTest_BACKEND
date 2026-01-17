package com.mycodingtest.api.review.dto.response;

import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewResponse(
        int difficultyLevel,
        int importanceLevel,
        ReviewStatus status,
        LocalDateTime reviewedAt) {
    public static ReviewResponse from(Review domain) {
        return new ReviewResponse(domain.getDifficultyLevel(), domain.getImportanceLevel(), domain.getStatus(), domain.getReviewedAt());
    }
}

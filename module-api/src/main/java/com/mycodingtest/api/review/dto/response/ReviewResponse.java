package com.mycodingtest.api.review.dto.response;

import com.mycodingtest.application.review.query.ReviewInfo;
import com.mycodingtest.domain.review.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewResponse(
        int difficultyLevel,
        int importanceLevel,
        ReviewStatus status,
        LocalDateTime reviewedAt) {

    public static ReviewResponse from(ReviewInfo info) {
        return new ReviewResponse(
                info.difficultyLevel(),
                info.importanceLevel(),
                info.status(),
                info.reviewedAt()
        );
    }
}

package com.mycodingtest.review.api.dto;

import com.mycodingtest.review.domain.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewRecentStatusResponse(
        ReviewStatus reviewStatus,
        LocalDateTime reviewedAt
) {
}

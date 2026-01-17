package com.mycodingtest.review.dto;

import com.mycodingtest.domain.review.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewRecentStatusResponse(
        ReviewStatus reviewStatus,
        LocalDateTime reviewedAt
) {
}

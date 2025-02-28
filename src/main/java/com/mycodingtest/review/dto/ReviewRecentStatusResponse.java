package com.mycodingtest.review.dto;

import java.time.LocalDateTime;

public record ReviewRecentStatusResponse(
        boolean reviewed,
        LocalDateTime reviewedAt
) {
}

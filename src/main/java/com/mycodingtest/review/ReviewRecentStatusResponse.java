package com.mycodingtest.review;

import java.time.LocalDateTime;

public record ReviewRecentStatusResponse(
        boolean reviewed,
        LocalDateTime reviewedAt
) {
}

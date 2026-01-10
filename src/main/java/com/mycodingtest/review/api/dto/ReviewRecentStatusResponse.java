package com.mycodingtest.review.api.dto;

import java.time.LocalDateTime;

public record ReviewRecentStatusResponse(

        boolean reviewed,
        LocalDateTime reviewedAt
) {
}

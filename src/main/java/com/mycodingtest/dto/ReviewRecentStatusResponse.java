package com.mycodingtest.dto;

import java.time.LocalDateTime;

public record ReviewRecentStatusResponse(
        boolean reviewed,
        LocalDateTime reviewedAt
) {
}

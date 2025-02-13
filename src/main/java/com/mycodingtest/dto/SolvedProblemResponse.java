package com.mycodingtest.dto;

import java.time.LocalDateTime;

public record SolvedProblemResponse(
        LocalDateTime recentSubmitAt,
        boolean isReviewed,
        boolean isFavorite
) {
}

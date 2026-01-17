package com.mycodingtest.api.review.dto.response;

public record ReviewCountStatusInToDoResponse(
        long count
) {
    public static ReviewCountStatusInToDoResponse from(Long count) {
        return new ReviewCountStatusInToDoResponse(count);
    }
}

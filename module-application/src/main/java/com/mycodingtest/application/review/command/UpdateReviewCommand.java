package com.mycodingtest.application.review.command;

public record UpdateReviewCommand(
        Long reviewId,
        Long userId
) {
    public static UpdateReviewCommand from(
            Long reviewId,
            Long userId
    ) {
        return new UpdateReviewCommand(
                reviewId,
                userId
        );
    }
}

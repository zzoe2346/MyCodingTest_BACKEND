package com.mycodingtest.review.api.dto;

import com.mycodingtest.review.domain.Review;

public class ReviewMapper {

    private ReviewMapper() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static ReviewResponse toResponse(Review entity) {
        return new ReviewResponse(
                entity.getDifficultyLevel(),
                entity.getImportanceLevel(),
                entity.isReviewed(),
                entity.getReviewedAt()
        );
    }

    public static ReviewRecentStatusResponse toRecentStatusResponse(Review entity) {
        return new ReviewRecentStatusResponse(
                entity.isReviewed(),
                entity.getReviewedAt()
        );
    }
}

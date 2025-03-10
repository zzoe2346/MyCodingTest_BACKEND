package com.mycodingtest.review;

import com.mycodingtest.review.dto.ReviewRecentStatusResponse;
import com.mycodingtest.review.dto.ReviewResponse;

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

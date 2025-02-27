package com.mycodingtest.mapper;

import com.mycodingtest.dto.ReviewRecentStatusResponse;
import com.mycodingtest.dto.ReviewResponse;
import com.mycodingtest.entity.Review;

public class ReviewMapper {

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

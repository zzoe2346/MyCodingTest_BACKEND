package com.mycodingtest.infra.review;

import com.mycodingtest.domain.review.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    public Review toDomain(ReviewEntity entity) {
        return Review.builder()
                .reviewId(entity.getId())
                .problemId(entity.getProblemId())
                .userId(entity.getUserId())
                .content(entity.getContent())
                .difficultyLevel(entity.getDifficultyLevel())
                .importanceLevel(entity.getImportanceLevel())
                .revisedCode(entity.getRevisedCode())
                .reviewedAt(entity.getReviewedAt())
                .status(entity.getStatus())
                .favorited(entity.isFavorited())
                .recentSubmitAt(entity.getRecentSubmitAt())
                .recentResult(entity.getRecentResult())
                .build();
    }

    public ReviewEntity toEntity(Review domain) {
        return ReviewEntity.builder()
                .problemId(domain.getProblemId())
                .userId(domain.getUserId())
                .content(domain.getContent())
                .difficultyLevel(domain.getDifficultyLevel())
                .importanceLevel(domain.getImportanceLevel())
                .revisedCode(domain.getRevisedCode())
                .reviewedAt(domain.getReviewedAt())
                .status(domain.getStatus())
                .favorited(domain.isFavorited())
                .recentSubmitAt(domain.getRecentSubmitAt())
                .recentResult(domain.getRecentResult())
                .build();
    }
}

package com.mycodingtest.api.review.dto.response;

import com.mycodingtest.application.review.query.ReviewInfo;
import com.mycodingtest.domain.review.ReviewStatus;

import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        Long userId,
        Long problemId,
        Boolean isFavorite,
        String sourceCode,//프론트에서 sourceCode로 받음
        String content,
        int difficultyLevel,
        int importanceLevel,
        ReviewStatus status,
        LocalDateTime reviewedAt,
        boolean reviewed) {

    public static ReviewResponse from(ReviewInfo info) {

        return new ReviewResponse(
                info.id(),
                info.userId(),
                info.problemId(),
                info.isFavorite(),
                info.revisedCode(),
                info.content(),
                info.difficultyLevel(),
                info.importanceLevel(),
                info.status(),
                info.reviewedAt(),
                info.reviewed()
        );
    }
}

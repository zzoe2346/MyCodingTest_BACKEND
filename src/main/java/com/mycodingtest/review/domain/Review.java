package com.mycodingtest.review.domain;

import com.mycodingtest.common.entity.BaseEntity;
import com.mycodingtest.common.exception.InvalidOwnershipException;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

    private Long problemId;
    private Long userId;
    private String content;
    private boolean reviewed;
    private Integer difficultyLevel = -1;
    private Integer importanceLevel = -1;
    private String sourceCode;
    private LocalDateTime reviewedAt;
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
    private boolean favorited;
    private LocalDateTime recentSubmitAt;
    private String recentResult;

    public Review(Long problemId, Long userId, String sourceCode, LocalDateTime recentSubmitAt, String recentResult) {
        this.problemId = problemId;
        this.userId = userId;
        this.sourceCode = sourceCode;
        this.content = "";
        this.reviewed = false;
        this.status = ReviewStatus.TO_DO;//NOT REVIEWED
    }

    public void startReview() {
        this.status = ReviewStatus.IN_PROGRESS;
    }

    public void completeReview() {
        this.status = ReviewStatus.COMPLETED;
        this.reviewedAt = LocalDateTime.now();
    }

    public void masterProblem() {
        this.status = ReviewStatus.MASTERED;
        this.reviewedAt = LocalDateTime.now();
    }

    public void updateRatingLevels(Integer difficultyLevel, Integer importanceLevel) {
        this.difficultyLevel = difficultyLevel;
        this.importanceLevel = importanceLevel;
    }

    public void validateOwnership(Long currentUserId) {
        if (!this.userId.equals(currentUserId)) {
            throw new InvalidOwnershipException();
        }
    }

    public void changeFavorite() {
        this.favorited = !this.favorited;
    }
}

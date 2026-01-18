package com.mycodingtest.infra.review;

import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewStatus;
import com.mycodingtest.infra.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <h3>오답 노트 / 리뷰 (Review)</h3>
 * <p>
 * 사용자가 푼 문제에 대해 복습, 메모, 난이도 평가를 수행하는 <b>학습의 핵심 단위</b>입니다.
 * 채점(Judgment)이 완료되면 자동으로 리뷰가 생성되며, 상태(Status) 기반의 생명주기를 가집니다.
 * </p>
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "review")
public class ReviewEntity extends BaseEntity {

    private Long problemId;
    private Long userId;

    private String content;
    private Integer difficultyLevel = -1;
    private Integer importanceLevel = -1;
    private String revisedCode;
    private LocalDateTime reviewedAt;
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    private boolean favorited;
    private boolean reviewed;

    private LocalDateTime recentSubmitAt;
    private String recentResult;

    public static ReviewEntity from(Review domain) {
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

    public void update(Review domain) {
        this.problemId = domain.getProblemId();
        this.userId = domain.getUserId();
        this.content = domain.getContent();
        this.difficultyLevel = domain.getDifficultyLevel();
        this.importanceLevel = domain.getImportanceLevel();
        this.revisedCode = domain.getRevisedCode();
        this.reviewedAt = domain.getReviewedAt();
        this.status = domain.getStatus();
        this.favorited = domain.isFavorited();
        this.recentSubmitAt = domain.getRecentSubmitAt();
        this.recentResult = domain.getRecentResult();
    }

    public Review toDomain() {
        return Review.from(
                this.getId(),
                this.problemId,
                this.userId,
                this.content,
                this.difficultyLevel,
                this.importanceLevel,
                this.revisedCode,
                this.reviewedAt,
                this.status,
                this.favorited,
                this.reviewed,
                this.recentSubmitAt,
                this.recentResult
        );
    }

}
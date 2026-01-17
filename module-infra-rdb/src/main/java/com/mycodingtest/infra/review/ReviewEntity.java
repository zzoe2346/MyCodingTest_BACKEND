package com.mycodingtest.infra.review;

import com.mycodingtest.domain.judgment.Judgment;
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

    /**
     * 리뷰 내용 (Markdown 지원 예정)
     */
    private String content;

    /**
     * 체감 난이도 (1~5)
     * <p>초기값 -1은 '평가되지 않음'을 의미합니다.</p>
     */
    private Integer difficultyLevel = -1;

    /**
     * 복습 중요도 (1~5)
     * <p>초기값 -1은 '설정되지 않음'을 의미합니다.</p>
     */
    private Integer importanceLevel = -1;

    /**
     * 사용자가 {@link Judgment}의 sourceCode를 수정한 코드
     * {@link Judgment}의 sourceCode는 로그용도로써 수정 불가하다.
     */
    private String revisedCode;

    /**
     * 리뷰 완료 시점
     */
    private LocalDateTime reviewedAt;

    /**
     * 리뷰 진행 상태 (TO_DO -> IN_PROGRESS -> COMPLETED)
     */
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;

    /**
     * 즐겨찾기/북마크 여부
     */
    private boolean favorited;

    private LocalDateTime recentSubmitAt;
    private String recentResult;

    public void apply(Review review) {
        this.problemId = review.getProblemId();
        this.userId = review.getUserId();
        this.content = review.getContent();
        this.difficultyLevel = review.getDifficultyLevel();
        this.importanceLevel = review.getImportanceLevel();
        this.revisedCode = review.getRevisedCode();
        this.reviewedAt = review.getReviewedAt();
        this.status = review.getStatus();
        this.favorited = review.isFavorited();
        this.recentSubmitAt = review.getRecentSubmitAt();
        this.recentResult = review.getRecentResult();
    }
}
package com.mycodingtest.review.domain;

import com.mycodingtest.common.entity.BaseEntity;
import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.judgment.domain.Judgment;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * <h3>오답 노트 / 리뷰 (Review)</h3>
 * <p>
 * 사용자가 푼 문제에 대해 복습, 메모, 난이도 평가를 수행하는 <b>학습의 핵심 단위</b>입니다.
 * 채점(Judgment)이 완료되면 자동으로 리뷰가 생성되며, 상태(Status) 기반의 생명주기를 가집니다.
 * </p>
 */
@Entity
@Getter
@NoArgsConstructor
public class Review extends BaseEntity {

    private Long problemId;
    private Long userId;

    /**
     * 리뷰 내용 (Markdown 지원 예정)
     */
    private String content;

    //TODO 이거 ReviewStatus 필드랑 중복이다. 제거 예정.
    /**
     * 리뷰 완료 여부 (Status와 연동됨)
     */
    private boolean reviewed;

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

    public Review(Long problemId, Long userId, String revisedCode, LocalDateTime recentSubmitAt, String recentResult) {
        this.problemId = problemId;
        this.userId = userId;
        this.revisedCode = revisedCode;
        this.content = "";
        this.reviewed = false;
        this.status = ReviewStatus.TO_DO;// 기본 상태: 리뷰 대기
        this.recentSubmitAt = recentSubmitAt;
        this.recentResult = recentResult;
    }

    /**
     * 리뷰 작성을 시작합니다.
     */
    public void startReview() {
        this.status = ReviewStatus.IN_PROGRESS;
    }

    /**
     * 리뷰 작성을 완료 처리합니다.
     * <p>상태가 COMPLETED로 변경되며, 완료 시간이 기록됩니다.</p>
     */
    public void completeReview() {
        this.status = ReviewStatus.COMPLETED;
        this.reviewedAt = LocalDateTime.now();
    }

    /**
     * 해당 문제를 완전히 마스터했음을 표시합니다.
     */
    public void masterProblem() {
        this.status = ReviewStatus.MASTERED;
        this.reviewedAt = LocalDateTime.now();
    }

    /**
     * 난이도와 중요도를 갱신합니다.
     * @param difficultyLevel 새로운 난이도
     * @param importanceLevel 새로운 중요도
     */
    public void updateRatingLevels(Integer difficultyLevel, Integer importanceLevel) {
        this.difficultyLevel = difficultyLevel;
        this.importanceLevel = importanceLevel;
    }

    /**
     * 리뷰의 소유권을 검증합니다.
     * <p>자신의 리뷰만 수정/조회할 수 있다는 보안 규칙을 도메인 레벨에서 강제합니다.</p>
     * 
     * @param currentUserId 요청한 사용자의 ID
     * @throws InvalidOwnershipException 소유자가 아닐 경우 발생
     */
    public void validateOwnership(Long currentUserId) {
        if (!this.userId.equals(currentUserId)) {
            throw new InvalidOwnershipException();
        }
    }

    /**
     * 즐겨찾기 상태를 토글(Toggle)합니다.
     */
    public void changeFavorite() {
        this.favorited = !this.favorited;
    }
}
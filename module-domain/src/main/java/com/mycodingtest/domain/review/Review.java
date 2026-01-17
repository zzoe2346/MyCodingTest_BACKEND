package com.mycodingtest.domain.review;

import com.mycodingtest.domain.common.exception.InvalidOwnershipException;
import com.mycodingtest.domain.judgment.Judgment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class Review {
    private Long reviewId;

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
    private ReviewStatus status;

    /**
     * 즐겨찾기/북마크 여부
     */
    private boolean favorited;

    private LocalDateTime recentSubmitAt;
    private Boolean reviewed;
    private String recentResult;

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

    public void update(Boolean isFavorite, Integer difficultyLevel, Integer importanceLevel, String code, String content, ReviewStatus status) {
        if (isFavorite != null) this.favorited = isFavorite;
        if (difficultyLevel != null) this.difficultyLevel = difficultyLevel;
        if (importanceLevel != null) this.importanceLevel = importanceLevel;
        if (code != null) this.revisedCode = code;
        if (content != null) this.content = content;
        if (status != null) {
            this.status = status;
            if (status == ReviewStatus.COMPLETED) {
                this.reviewedAt = LocalDateTime.now();
            }
        }
    }

    public void onJudgmentCreated(LocalDateTime recentSubmitAt, String recentResult) {
        this.reviewed = false;
        this.status = ReviewStatus.TO_DO;
        this.recentSubmitAt = recentSubmitAt;
        this.recentResult = recentResult;
    }
}

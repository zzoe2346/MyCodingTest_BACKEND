package com.mycodingtest.domain.review;

import com.mycodingtest.domain.common.exception.InvalidOwnershipException;
import com.mycodingtest.domain.judgment.Judgment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * <h3>복습 기록 (Review)</h3>
 * <p>
 * 사용자의 알고리즘 문제 풀이에 대한 복습 기록을 관리하는 도메인 엔티티입니다.
 * 하나의 문제에 대해 사용자별로 하나의 Review가 존재하며,
 * 여러 번의 채점({@link Judgment}) 결과를 종합하여 학습 진도를 추적합니다.
 * </p>
 * <p>
 * <b>핵심 비즈니스 규칙:</b>
 * <ul>
 * <li>소유권 검증: 본인의 리뷰만 수정 가능 ({@link #validateOwnership(Long)})</li>
 * <li>상태 전이: TO_DO → IN_PROGRESS → COMPLETED → MASTERED</li>
 * <li>새 채점 시 상태 초기화: {@link #onJudgmentCreated(LocalDateTime, String)}</li>
 * </ul>
 * </p>
 *
 * @see ReviewStatus
 * @see ReviewRepository
 */
@AllArgsConstructor
@Builder
@Getter
public class Review {

    /**
     * 시스템 내부 고유 식별자
     */
    private Long id;

    /**
     * 리뷰 대상 문제 ID
     */
    private Long problemId;

    /**
     * 리뷰 소유자(사용자) ID
     */
    private Long userId;

    /**
     * 리뷰 내용 (오답 노트, Markdown 지원 예정)
     */
    private String content;

    /**
     * 체감 난이도 (1~5)
     * <p>
     * 초기값 -1은 '평가되지 않음'을 의미합니다.
     * </p>
     */
    private Integer difficultyLevel = -1;

    /**
     * 복습 중요도 (1~5)
     * <p>
     * 초기값 -1은 '설정되지 않음'을 의미합니다.
     * </p>
     */
    private Integer importanceLevel = -1;

    /**
     * 사용자가 수정한 코드
     * <p>
     * {@link Judgment}의 sourceCode는 원본 로그용으로 수정 불가하며,
     * 사용자가 리뷰 시 수정한 코드는 이 필드에 저장됩니다.
     * </p>
     */
    private String revisedCode;

    /**
     * 리뷰 완료 시점
     * <p>
     * 상태가 COMPLETED로 변경될 때 자동으로 설정됩니다.
     * </p>
     */
    private LocalDateTime reviewedAt;

    /**
     * 리뷰 진행 상태
     *
     * @see ReviewStatus
     */
    private ReviewStatus status;

    /**
     * 즐겨찾기/북마크 여부
     */
    private boolean favorited;

    /**
     * 가장 최근 채점 제출 시점
     */
    private LocalDateTime recentSubmitAt;

    /**
     * 리뷰 완료 여부
     */
    private Boolean reviewed;

    /**
     * 가장 최근 채점 결과 텍스트 (예: "맞았습니다!!")
     */
    private String recentResult;

    /**
     * 리뷰의 소유권을 검증합니다.
     * <p>
     * 자신의 리뷰만 수정/조회할 수 있다는 보안 규칙을 도메인 레벨에서 강제합니다.
     * </p>
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
     * 리뷰 정보를 부분 업데이트합니다.
     * <p>
     * null이 아닌 값만 업데이트되며, 상태가 COMPLETED로 변경되면 reviewedAt이 현재 시각으로 설정됩니다.
     * </p>
     *
     * @param isFavorite      즐겨찾기 여부
     * @param difficultyLevel 체감 난이도
     * @param importanceLevel 복습 중요도
     * @param code            수정된 코드
     * @param content         리뷰 내용
     * @param status          진행 상태
     */
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

    /**
     * 새로운 채점이 생성되었을 때 호출되는 이벤트 핸들러입니다.
     * <p>
     * 리뷰 상태를 TO_DO로 초기화하여 사용자가 다시 복습할 수 있도록 합니다.
     * </p>
     *
     * @param recentSubmitAt 새 채점의 제출 시각
     * @param recentResult   새 채점의 결과 텍스트
     */
    public void onJudgmentCreated(LocalDateTime recentSubmitAt, String recentResult) {
        this.reviewed = false;
        this.status = ReviewStatus.TO_DO;
        this.recentSubmitAt = recentSubmitAt;
        this.recentResult = recentResult;
    }
}

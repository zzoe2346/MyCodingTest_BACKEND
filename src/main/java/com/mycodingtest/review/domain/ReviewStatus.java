package com.mycodingtest.review.domain;

/**
 * <h3>리뷰 진행 상태 (ReviewStatus)</h3>
 * <p>
 * 학습(복습)의 진행 단계를 정의하는 생명주기(Lifecycle) 상태 값입니다.
 * </p>
 */
public enum ReviewStatus {
    /**
     * <b>리뷰 대기</b>
     * <p>채점 결과는 수집되었으나, 아직 사용자가 내용을 확인하거나 정리하지 않은 초기 상태입니다.</p>
     */
    TO_DO,

    /**
     * <b>리뷰 진행 중</b>
     * <p>사용자가 오답 노트 작성을 시작했거나 내용을 수정 중인 상태입니다.</p>
     */
    IN_PROGRESS,

    /**
     * <b>리뷰 완료</b>
     * <p>1차적인 복습 및 오답 노트 작성이 완료된 상태입니다.</p>
     */
    COMPLETED,

    /**
     * <b>완전 정복 (Mastered)</b>
     * <p>반복적인 복습을 통해 해당 문제를 완벽히 이해하여 더 이상의 리뷰가 필요 없는 상태입니다.</p>
     */
    MASTERED
}
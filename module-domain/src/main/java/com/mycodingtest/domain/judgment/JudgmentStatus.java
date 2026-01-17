package com.mycodingtest.domain.judgment;

/**
 * <h3>채점 결과 상태 (JudgmentStatus)</h3>
 * <p>
 * 문제 풀이의 성공 여부를 나타내는 열거형입니다.
 * 시스템 내부적으로 채점 결과의 종류를 구분하는 기준이 됩니다.
 * </p>
 */
public enum JudgmentStatus {
    /**
     * 정답
     */
    SUCCESS,

    /**
     * 오답
     */
    FAIL
}
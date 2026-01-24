package com.mycodingtest.domain.judgment;

import lombok.Builder;
import lombok.Getter;

/**
 * <h3>채점 (Judgment)</h3>
 * <p>
 * 사용자가 특정 플랫폼(BOJ, Programmers 등)에서 문제를 풀고 제출한 개별 채점 결과를 나타내는 Aggregate
 * Root입니다.
 * 하나의 문제는 여러 번의 채점 기록을 가질 수 있으며, 본 시스템은 이 채점 기록을 수집하여 리뷰 데이터를 생성하는 기초 자료로
 * 활용합니다.
 * </p>
 */
@Getter
@Builder
public class Judgment {

    /**
     * 고유 식별자
     */
    private Long id;

    /**
     * 채점된 문제의 식별자
     */
    private Long problemId;

    /**
     * 채점을 수행한 사용자의 식별자
     */
    private Long userId;

    /**
     * 제출 정보 (플랫폼, 결과, 메타데이터, 소스코드 등)
     */
    private SubmissionInfo submissionInfo;

    /**
     * 새로운 채점 기록을 생성합니다.
     *
     * @param problemId      문제 ID
     * @param userId         사용자 ID
     * @param submissionInfo 제출 정보
     * @return 생성된 Judgment 인스턴스
     * @throws IllegalArgumentException 필수 값이 누락된 경우
     */
    public static Judgment from(Long problemId,
                                Long userId,
                                SubmissionInfo submissionInfo) {
        if (userId == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다");
        }
        if (submissionInfo == null) {
            throw new IllegalArgumentException("제출 정보는 필수입니다");
        }
        return Judgment.builder()
                .problemId(problemId)
                .userId(userId)
                .submissionInfo(submissionInfo)
                .build();
    }
}

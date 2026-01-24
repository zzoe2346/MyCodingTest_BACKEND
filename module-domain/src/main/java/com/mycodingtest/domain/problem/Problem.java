package com.mycodingtest.domain.problem;

import com.mycodingtest.domain.common.Platform;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * <h3>문제 (Problem)</h3>
 * <p>
 * 알고리즘 문제 풀이 플랫폼의 개별 문제를 나타내는 도메인 엔티티입니다.
 * 하나의 문제는 특정 플랫폼에 속하며, 여러 개의 {@link com.mycodingtest.domain.judgment.Judgment}와
 * {@link com.mycodingtest.domain.review.Review}를 가질 수 있습니다.
 * </p>
 * <p>
 * <b>복합 식별자:</b> {@code problemNumber}와 {@code platform}의 조합으로 유일하게 식별됩니다.
 * </p>
 */
@AllArgsConstructor
@Builder
@Getter
public class Problem {

    /**
     * 시스템 내부 고유 식별자
     */
    private Long id;

    /**
     * 문제 번호 (예: 백준 1000번)
     * <p>
     * 플랫폼에서 부여한 원본 문제 번호입니다.
     * </p>
     */
    private Integer problemNumber;

    /**
     * 문제 제목
     * <p>
     * 플랫폼에서 제공하는 원본 문제 제목입니다.
     * </p>
     */
    private String problemTitle;

    /**
     * 출처 플랫폼 (BOJ, Programmers 등)
     */
    private Platform platform;

    /**
     * 새로운 문제를 생성합니다.
     *
     * @param problemNumber 문제 번호
     * @param problemTitle  문제 제목
     * @param platform      출처 플랫폼
     * @return 생성된 Problem 인스턴스
     * @throws IllegalArgumentException 필수 값이 누락된 경우
     */
    public static Problem from(Integer problemNumber, String problemTitle, Platform platform) {
        if (problemNumber == null || problemNumber <= 0) {
            throw new IllegalArgumentException("문제 번호는 양수여야 합니다");
        }
        if (platform == null) {
            throw new IllegalArgumentException("플랫폼은 필수입니다");
        }
        return Problem.builder()
                .problemNumber(problemNumber)
                .problemTitle(problemTitle)
                .platform(platform)
                .build();
    }

    public static Problem from(Long problemId, Integer problemNumber, String problemTitle, Platform platform) {
        if (problemNumber == null || problemNumber <= 0) {
            throw new IllegalArgumentException("문제 번호는 양수여야 합니다");
        }
        if (platform == null) {
            throw new IllegalArgumentException("플랫폼은 필수입니다");
        }
        return Problem.builder()
                .id(problemId)
                .problemNumber(problemNumber)
                .problemTitle(problemTitle)
                .platform(platform)
                .build();
    }

}

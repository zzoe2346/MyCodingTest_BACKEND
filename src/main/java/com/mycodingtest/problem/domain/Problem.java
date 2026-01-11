package com.mycodingtest.problem.domain;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * <h3>문제 (Problem)</h3>
 * <p>
 * 알고리즘 문제 자체에 대한 정보를 담는 불변(Invariant) 성격의 엔티티입니다.
 * 플랫폼(Platform)과 문제 번호(ProblemNumber)의 조합으로 유일성이 식별됩니다.
 * </p>
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Problem extends BaseEntity {

    /**
     * 문제 번호 (예: 백준 1000번)
     */
    private Integer problemNumber;

    /**
     * 문제 제목
     */
    private String problemTitle;

    /**
     * 출처 플랫폼 (BOJ, PROGRAMMERS 등)
     */
    @Enumerated(EnumType.STRING)
    private Platform platform;

    /**
     * 정적 팩토리 메서드
     */
    public static Problem of(Integer problemNumber, String problemTitle, Platform platform) {
        return new Problem(problemNumber, problemTitle, platform);
    }
}
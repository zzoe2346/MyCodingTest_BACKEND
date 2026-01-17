package com.mycodingtest.infra.problem;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.infra.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

/**
 * <h3>문제 (Problem)</h3>
 * <p>
 * 알고리즘 문제 자체에 대한 정보를 담는 불변(Invariant) 성격의 엔티티입니다.
 * 플랫폼(Platform)과 문제 번호(ProblemNumber)의 조합으로 유일성이 식별됩니다.
 * </p>
 */
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "problem")
public class ProblemEntity extends BaseEntity {

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

}
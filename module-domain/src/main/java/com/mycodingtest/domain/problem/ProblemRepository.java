package com.mycodingtest.domain.problem;

import com.mycodingtest.domain.common.Platform;

import java.util.List;
import java.util.Optional;

/**
 * <h3>Problem Repository Interface (Domain Layer)</h3>
 * <p>
 * {@link ProblemEntity} 엔티티에 대한 데이터 접근을 담당합니다.
 * 도메인 영역에 위치하며, 구체적인 기술(JPA 등)에 의존하지 않습니다.
 * </p>
 */
public interface ProblemRepository {

    /**
     * 문제를 저장합니다.
     *
     * @param problem 저장할 문제 엔티티
     * @return 저장된 문제 엔티티
     */
    Problem save(Problem problem);

    /**
     * 문제 번호와 플랫폼 정보를 복합키처럼 사용하여 문제를 조회합니다.
     *
     * @param problemNumber 문제 번호
     * @param platform      플랫폼 (BOJ 등)
     * @return 문제 엔티티 (Optional)
     */
    Optional<Problem> findProblem(Integer problemNumber, Platform platform);

    List<Problem> findAllByIdIn(List<Long> problemIds);
}

package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;

import java.util.List;

/**
 * <h3>Judgment Repository Interface (Domain Layer)</h3>
 * <p>
 * 채점 기록(Judgment)을 관리하는 저장소의 추상화된 인터페이스입니다.
 * </p>
 * <p>
 * <b>DIP(Dependency Inversion Principle) 적용:</b><br>
 * 이 인터페이스는 도메인 영역에 위치하며, 구체적인 기술(JPA, MyBatis, Memory 등)에 의존하지 않습니다.
 * 실제 구현체는 Infrastructure 계층에 존재합니다.
 * </p>
 */
public interface JudgmentRepository {

    /**
     * 채점 결과를 저장합니다.
     *
     * @param judgment 저장할 채점 도메인 객체
     * @return 저장된 객체
     */
    Judgment save(Judgment judgment);

    /**
     * 외부 플랫폼의 제출 ID가 이미 존재하는지 확인합니다.
     * <p>
     * 중복 수집을 방지하기 위한 핵심 비즈니스 로직에 사용됩니다.
     * </p>
     *
     * @param submissionId 외부 플랫폼의 제출 번호
     * @param platform 플랫폼 종류
     * @return 존재 여부 (true: 이미 존재함)
     */
    boolean existsBySubmissionId(Long submissionId, Platform platform);

    /**
     * 채점 결과를 삭제합니다.
     * <p>
     * 사용자 본인의 채점 기록만 삭제할 수 있도록 userId를 함께 검증합니다.
     * </p>
     *
     * @param id     삭제할 채점 기록의 ID
     * @param userId 요청한 사용자의 ID
     */
    void deleteByIdAndUserId(Long id, Long userId);

    /**
     * 특정 문제에 대한 사용자의 모든 채점 기록을 조회합니다.
     *
     * @param problemId 문제 ID
     * @param userId    사용자 ID
     * @return 해당 문제에 대한 채점 기록 목록
     */
    List<Judgment> findByProblemIdAndUserId(Long problemId, Long userId);

}

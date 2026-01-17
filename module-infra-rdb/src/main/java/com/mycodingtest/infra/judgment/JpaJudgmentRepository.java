package com.mycodingtest.infra.judgment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaJudgmentRepository extends JpaRepository<JudgmentEntity, Long> {
    boolean existsBySubmissionId(Long submissionId);

    void deleteByIdAndUserId(Long id, Long userId);

    List<JudgmentEntity> findByProblemIdAndUserId(Long problemId, Long userId);
}

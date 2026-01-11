package com.mycodingtest.judgment.infrastructure;

import com.mycodingtest.judgment.domain.Judgment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaJudgmentRepository extends JpaRepository<Judgment, Long> {
    boolean existsBySubmissionId(Long submissionId);
    void deleteByIdAndUserId(Long id, Long userId);
    List<Judgment> findByProblemIdAndUserId(Long problemId, Long userId);
}

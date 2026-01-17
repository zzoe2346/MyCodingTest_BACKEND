package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.common.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaJudgmentRepository extends JpaRepository<JudgmentEntity, Long> {

    boolean existsBySubmissionId(Long submissionId);

    void deleteByIdAndUserId(Long id, Long userId);

    List<JudgmentEntity> findByProblemIdAndUserId(Long problemId, Long userId);

    boolean existsBySubmissionIdAndPlatform(Long submissionId, Platform platform);

}

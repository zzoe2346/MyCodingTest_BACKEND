package com.mycodingtest.infra.problem;

import com.mycodingtest.domain.common.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProblemRepository extends JpaRepository<ProblemEntity, Long> {
    Optional<ProblemEntity> findByProblemNumberAndPlatform(Integer problemNumber, Platform platform);
}

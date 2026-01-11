package com.mycodingtest.problem.infrastructure;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByProblemNumberAndPlatform(Integer problemNumber, Platform platform);
}

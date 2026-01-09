package com.mycodingtest.judgmentresult;

import com.mycodingtest.problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JudgmentResultRepository extends JpaRepository<JudgmentResult, Long> {
    @Query("SELECT jr " +
            "FROM JudgmentResult jr " +
            "JOIN FETCH jr.solvedProblem sp " +
            "JOIN FETCH jr.user u " +
            "WHERE sp.id = :solvedProblemId AND u.id = :userId " +
            "ORDER BY jr.submissionId DESC")
    List<JudgmentResult> findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(@Param("solvedProblemId") Long solvedProblemId, @Param("userId") Long userId);

    boolean existsBySubmissionId(Long submissionId);

    List<JudgmentResult> findAllBySolvedProblem(Problem problem);
}


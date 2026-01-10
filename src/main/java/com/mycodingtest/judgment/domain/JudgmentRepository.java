package com.mycodingtest.judgment.domain;

import com.mycodingtest.problem.domain.Problem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JudgmentRepository extends JpaRepository<Judgment, Long> {
    @Query("SELECT jr " +
            "FROM Submission jr " +
            "JOIN FETCH jr.solvedProblem sp " +
            "JOIN FETCH jr.user u " +
            "WHERE sp.id = :solvedProblemId AND u.id = :userId " +
            "ORDER BY jr.submissionId DESC")
// 문제정보 채점결과 제출 코드
    List<Submission> findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(@Param("solvedProblemId") Long solvedProblemId, @Param("userId") Long userId);

    boolean existsBySubmissionId(Long submissionId);

    List<Submission> findAllBySolvedProblem(Problem problem);
}


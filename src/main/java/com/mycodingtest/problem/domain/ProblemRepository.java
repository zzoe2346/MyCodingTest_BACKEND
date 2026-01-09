package com.mycodingtest.problem.domain;

import com.mycodingtest.problem.dto.ProblemWithReviewResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {
    Optional<Problem> findByUserIdAndProblemNumber(Long userId, int problemNumber);

    @Query("SELECT new com.mycodingtest.problem.dto.ProblemWithReviewResponse(sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed,r.reviewedAt) " +
            "FROM Problem sp " +
            "LEFT JOIN sp.review r " +
            "WHERE sp.user.id = :userId ")
    Page<ProblemWithReviewResponse> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.mycodingtest.problem.dto.ProblemWithReviewResponse(sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed,r.reviewedAt) " +
            "FROM Problem sp " +
            "LEFT JOIN sp.review r " +
            "WHERE sp.user.id = :userId " +
            "AND r.reviewed = :reviewed")
    Page<ProblemWithReviewResponse> findAllByUserIdAndReviewed(@Param("userId") Long userId, @Param("reviewed") boolean reviewed, Pageable pageable);

    @Query("SELECT new com.mycodingtest.problem.dto.ProblemWithReviewResponse(sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed,r.reviewedAt) " +
            "FROM Problem sp " +
            "LEFT JOIN sp.review r " +
            "WHERE sp.user.id = :userId " +
            "AND sp.favorited = true")
    Page<ProblemWithReviewResponse> findAllByUserIdAndFavoriteIsTrue(Long userId, Pageable pageable);

    @Query("SELECT new com.mycodingtest.problem.dto.ProblemWithReviewResponse(" +
            "sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, " +
            "r.id, r.difficultyLevel, r.importanceLevel, r.reviewed, r.reviewedAt) " +
            "FROM Problem sp " +
            "LEFT JOIN sp.review r " +
            "JOIN sp.tags t " +
            "WHERE t.tagId = :tagId " +
            "AND sp.user.id = :userId")
    Page<ProblemWithReviewResponse> findByAlgorithmTagAndUserId(@Param("tagId") Long tagId, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT t.tagId " +
            "FROM Problem sp " +
            "JOIN sp.tags t " +
            "WHERE sp.user.id = :userId")
    List<Long> findDistinctTagIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT t.tagId " +
            "FROM Problem  sp " +
            "JOIN sp.tags t " +
            "WHERE sp.id = :solvedProblemId")
    List<Integer> findTagIdsBySolvedProblemId(@Param("solvedProblemId") Long solvedProblemId);
}

package com.mycodingtest.solvedproblem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SolvedProblemRepository extends JpaRepository<SolvedProblem, Long> {
    Optional<SolvedProblem> findByUserIdAndProblemNumber(Long userId, int problemNumber);

    @Query("SELECT new com.mycodingtest.solvedproblem.SolvedProblemWithReviewResponse(sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed,r.reviewedAt) " +
            "FROM SolvedProblem sp " +
            "LEFT JOIN sp.review r " +
            "WHERE sp.user.id = :userId ")
    Page<SolvedProblemWithReviewResponse> findAllByUserId(@Param("userId") Long userId, Pageable pageable);

    @Query("SELECT new com.mycodingtest.solvedproblem.SolvedProblemWithReviewResponse(sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed,r.reviewedAt) " +
            "FROM SolvedProblem sp " +
            "LEFT JOIN sp.review r " +
            "WHERE sp.user.id = :userId " +
            "AND r.reviewed = :reviewed")
    Page<SolvedProblemWithReviewResponse> findAllByUserIdAndReviewed(@Param("userId") Long userId, @Param("reviewed") boolean reviewed, Pageable pageable);

    @Query("SELECT new com.mycodingtest.solvedproblem.SolvedProblemWithReviewResponse(sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, r.id, r.difficultyLevel, r.importanceLevel, r.reviewed,r.reviewedAt) " +
            "FROM SolvedProblem sp " +
            "LEFT JOIN sp.review r " +
            "WHERE sp.user.id = :userId " +
            "AND sp.favorited = true")
    Page<SolvedProblemWithReviewResponse> findAllByUserIdAndFavoriteIsTrue(Long userId, Pageable pageable);

    @Query("SELECT new com.mycodingtest.solvedproblem.SolvedProblemWithReviewResponse(" +
            "sp.id, sp.problemNumber, sp.problemTitle, sp.recentSubmitAt, sp.recentResultText, sp.favorited, " +
            "r.id, r.difficultyLevel, r.importanceLevel, r.reviewed, r.reviewedAt) " +
            "FROM SolvedProblem sp " +
            "LEFT JOIN sp.review r " +
            "JOIN sp.tags t " +
            "WHERE t.tagId = :tagId " +
            "AND sp.user.id = :userId")
    Page<SolvedProblemWithReviewResponse> findByAlgorithmTagAndUserId(@Param("tagId") Long tagId, @Param("userId") Long userId, Pageable pageable);

    @Query("SELECT DISTINCT t.tagId " +
            "FROM SolvedProblem sp " +
            "JOIN sp.tags t " +
            "WHERE sp.user.id = :userId")
    List<Long> findDistinctTagIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT t.tagId " +
            "FROM SolvedProblem  sp " +
            "JOIN sp.tags t " +
            "WHERE sp.id = :solvedProblemId")
    List<Integer> findTagIdsBySolvedProblemId(@Param("solvedProblemId") Long solvedProblemId);
}

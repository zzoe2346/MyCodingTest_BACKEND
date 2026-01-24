package com.mycodingtest.infra.review;

import com.mycodingtest.domain.review.ReviewStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaReviewRepository extends JpaRepository<ReviewEntity, Long> {

    long countAllByStatusAndUserId(ReviewStatus status, Long userId);

    Optional<ReviewEntity> findByProblemIdAndUserId(Long problemId, Long userId);

    Page<ReviewEntity> findAllByUserIdAndStatus(Long userId, ReviewStatus status, Pageable pageable);

    Page<ReviewEntity> findAllByUserId(Long userId, Pageable pageable);
}
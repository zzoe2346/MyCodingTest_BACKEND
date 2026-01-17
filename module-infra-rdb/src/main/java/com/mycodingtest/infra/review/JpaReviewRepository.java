package com.mycodingtest.infra.review;

import com.mycodingtest.domain.review.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<ReviewEntity, Long> {

    long countAllByStatusAndUserId(ReviewStatus status, Long userId);

}
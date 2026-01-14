package com.mycodingtest.review.infrastructure;

import com.mycodingtest.review.domain.Review;
import com.mycodingtest.review.domain.ReviewStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
    
    long countAllByStatusAndUserId(ReviewStatus status, Long userId);

}
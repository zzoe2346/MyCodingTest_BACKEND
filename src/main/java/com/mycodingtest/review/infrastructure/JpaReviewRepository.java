package com.mycodingtest.review.infrastructure;

import com.mycodingtest.review.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaReviewRepository extends JpaRepository<Review, Long> {
    
    long countAllByReviewedIsFalseAndUserId(Long userId);

}
package com.mycodingtest.review;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    int countAllByReviewedIsFalseAndUserId(Long userId);

    @EntityGraph(attributePaths = "user")
    Optional<Review> findById(Long reviewId);
}

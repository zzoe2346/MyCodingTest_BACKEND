package com.mycodingtest.review.infrastructure;

import com.mycodingtest.review.domain.Review;
import com.mycodingtest.review.domain.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JpaReviewRepository jpaReviewRepository;

    @Override
    public Review save(Review review) {
        return jpaReviewRepository.save(review);
    }

    @Override
    public Optional<Review> findById(Long id) {
        return jpaReviewRepository.findById(id);
    }

    @Override
    public long countAllByReviewedIsFalseAndUserId(Long userId) {
        return jpaReviewRepository.countAllByReviewedIsFalseAndUserId(userId);
    }
}
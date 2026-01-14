package com.mycodingtest.review.infrastructure;

import com.mycodingtest.review.domain.Review;
import com.mycodingtest.review.domain.ReviewRepository;
import com.mycodingtest.review.domain.ReviewStatus;
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
    public long countPendingReviewsByUserId(Long userId) {
        // Pending = TO_DO 상태인 것들
        return jpaReviewRepository.countAllByStatusAndUserId(ReviewStatus.TO_DO, userId);
    }
}
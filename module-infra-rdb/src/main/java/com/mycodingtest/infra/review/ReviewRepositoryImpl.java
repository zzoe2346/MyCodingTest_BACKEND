package com.mycodingtest.infra.review;

import com.mycodingtest.domain.common.DomainPage;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.domain.review.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepository {

    private final JpaReviewRepository repository;

    @Override
    public Review create(Review review) {
        ReviewEntity saved = repository.save(ReviewEntity.from(review));
        return saved.toDomain();
    }

    @Override
    public Review update(Review review) {
        ReviewEntity entity = repository.findById(review.getId())
                .orElseThrow();
        entity.update(review);
        return entity.toDomain();
    }

    @Override
    public Optional<Review> findById(Long id) {
        return repository.findById(id).map(ReviewEntity::toDomain);
    }

    @Override
    public long countPendingReviewsByUserId(Long userId) {
        // Pending = TO_DO 상태인 것들
        return repository.countAllByStatusAndUserId(ReviewStatus.TO_DO, userId);
    }

    @Override
    public Optional<Review> findByProblemIdAndUserId(Long problemId, Long userId) {
        return repository.findByProblemIdAndUserId(problemId, userId)
                .map(ReviewEntity::toDomain);
    }

    @Override
    public DomainPage<Review> findAllByUserIdAndStatus(Long userId, ReviewStatus filter, int page, int size) {
        Page<ReviewEntity> entityPage = repository.findAllByUserIdAndStatus(userId, filter, Pageable.ofSize(size).withPage(page));

        return new DomainPage<>(
                entityPage.getContent().stream().map(ReviewEntity::toDomain).toList(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.isLast()
        );
    }

    @Override
    public DomainPage<Review> findAllByUserId(Long userId, int page, int size) {
        Page<ReviewEntity> entityPage = repository.findAllByUserId(userId, Pageable.ofSize(size).withPage(page));

        return new DomainPage<>(
                entityPage.getContent().stream().map(ReviewEntity::toDomain).toList(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.isLast()
        );
    }

    @Override
    public DomainPage<Review> findAllByUserIdAndFavorite(Long userId, int page, int size) {
        Page<ReviewEntity> entityPage = repository.findAllByUserIdAndFavorited(userId, true, Pageable.ofSize(size).withPage(page));

        return new DomainPage<>(
                entityPage.getContent().stream().map(ReviewEntity::toDomain).toList(),
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.getNumber(),
                entityPage.getSize(),
                entityPage.isLast()
        );
    }
}
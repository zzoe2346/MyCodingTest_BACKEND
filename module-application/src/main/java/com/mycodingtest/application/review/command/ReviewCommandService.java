package com.mycodingtest.application.review.command;

import com.mycodingtest.domain.common.exception.ResourceNotFoundException;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>리뷰 및 오답 노트 서비스 (ReviewCommandService)</h3>
 * <p>
 * 사용자의 학습 기록(Review)에 대한 상태 변경, 평가, 조회 등의 비즈니스 로직을 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ReviewCommandService {

    private final ReviewRepository reviewRepository;

    /**
     * 새로운 리뷰를 생성합니다. (수집 엔진에 의해 호출됨)
     */
    @Transactional
    public void createReview(CreateReviewCommand command) {
        reviewRepository.findByProblemIdAndUserId(command.problemId(), command.userId())
                .ifPresent(existingReview -> {
                    existingReview.onJudgmentCreated(command.submittedAt(), command.resultText());
                    reviewRepository.update(existingReview);
                });
        reviewRepository.create(
                Review.from(
                        command.problemId(),
                        command.userId(),
                        command.sourceCode(),
                        command.submittedAt(),
                        command.resultText()));
    }

    @Transactional
    public UpdateReviewResult updateReview(UpdateReviewCommand command) {
        Review review = reviewRepository.findById(command.reviewId())
                .orElseThrow(ResourceNotFoundException::new);
        review.validateOwnership(command.userId());
        review.update(
                command.isFavorite(),
                command.difficultyLevel(),
                command.importanceLevel(),
                command.code(),
                command.content(),
                command.status());
        reviewRepository.update(review);
        return UpdateReviewResult.from(review);
    }

}
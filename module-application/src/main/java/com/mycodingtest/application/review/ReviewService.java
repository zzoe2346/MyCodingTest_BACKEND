package com.mycodingtest.application.review;

import com.mycodingtest.application.review.dto.UpdateReviewCommand;
import com.mycodingtest.domain.common.exception.ResourceNotFoundException;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.application.review.dto.CreateReviewCommand;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <h3>리뷰 및 오답 노트 서비스 (ReviewService)</h3>
 * <p>
 * 사용자의 학습 기록(Review)에 대한 상태 변경, 평가, 조회 등의 비즈니스 로직을 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    /**
     * 단건 리뷰 정보를 조회합니다. (소유권 검증 포함)
     */
    @Transactional(readOnly = true)
    public Review getReview(Long reviewId, Long userId) {
        return getReviewAndValidateOwnership(reviewId, userId);
    }

    /**
     * 현재 사용자가 아직 복습하지 않은(TO_DO) 문제의 개수를 반환합니다.
     */
    @Transactional(readOnly = true)
    public long getReviewCountStatusInToDo(Long userId) {
        return reviewRepository.countPendingReviewsByUserId(userId);
    }

    /**
     * 공통 로직: 리뷰 존재 여부 확인 및 소유권 검증
     */
    private Review getReviewAndValidateOwnership(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);

        review.validateOwnership(userId);

        return review;
    }

    /**
     * 새로운 리뷰를 생성합니다. (수집 엔진에 의해 호출됨)
     */
    @Transactional
    public void createReview(CreateReviewCommand command) {
        reviewRepository.findByProblemIdAndUserId(command.problemId(), command.userId())
                .ifPresent(existingReview -> {
                    existingReview.onJudgmentCreated(command.submittedAt(), command.judgmentId(), command.resultText());
                    reviewRepository.update(existingReview);
                });
        reviewRepository.create(
                Review.builder()
                        .problemId(command.problemId())
                        .userId(command.userId())
                        .revisedCode(command.sourceCode())
                        .recentSubmitAt(command.submittedAt())
                        .recentResult(command.resultText())
                        .build()
        );
    }

    public Review updateReview(UpdateReviewCommand command) {
        Review review = reviewRepository.findById(command.reviewId())
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다."));

        // 권한 체크 등 비즈니스 검증 로직
//        validateOwner(review, command.getUserId());

        // 도메인 모델에게 업데이트 위임
        review.update(
                command.isFavorite(),
                command.difficultyLevel(),
                command.importanceLevel(),
                command.code(),
                command.content(),
                command.status()
        );
        return review;
    }
}
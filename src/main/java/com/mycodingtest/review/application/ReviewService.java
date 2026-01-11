package com.mycodingtest.review.application;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.review.api.dto.*;
import com.mycodingtest.review.domain.Review;
import com.mycodingtest.review.domain.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

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
     * 리뷰의 체감 난이도와 중요도를 업데이트합니다.
     */
    @Transactional
    public void updateReviewRatingLevels(ReviewRatingLevelsUpdateRequest request, Long reviewId, Long userId) {
        Review review = getReviewAndValidateOwnership(reviewId, userId);
        review.updateRatingLevels(request.difficultyLevel(), request.importanceLevel());
    }

    /**
     * 단건 리뷰 정보를 조회합니다. (소유권 검증 포함)
     */
    @Transactional(readOnly = true)
    public Review getReview(Long reviewId, Long userId) {
        return getReviewAndValidateOwnership(reviewId, userId);
    }

    /**
     * <b>리뷰 상태 완료 처리</b>
     * <p>사용자가 복습을 완료했을 때 호출되며, 상태를 COMPLETED로 변경합니다.</p>
     */
    @Transactional
    public ReviewRecentStatusResponse updateReviewStatus(Long reviewId, Long userId) {
        Review review = getReviewAndValidateOwnership(reviewId, userId);
        review.completeReview();
        return ReviewMapper.toRecentStatusResponse(review);
    }

    /**
     * 현재 사용자가 아직 복습하지 않은(TO_DO) 문제의 개수를 반환합니다.
     */
    @Transactional(readOnly = true)
    public WaitReviewCountResponse getWaitReviewCount(Long userId) {
        return new WaitReviewCountResponse(reviewRepository.countAllByReviewedIsFalseAndUserId(userId));
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
    public void createReview(Long problemId, Long userId, String sourceCode, LocalDateTime submittedAt, String resultText) {
        reviewRepository.save(new Review(problemId, userId, sourceCode, submittedAt, resultText));
    }

    /**
     * 즐겨찾기(북마크) 상태를 토글합니다.
     */
    @Transactional
    public void changeFavorite(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow();
        review.validateOwnership(userId);
        review.changeFavorite();
        reviewRepository.save(review);
    }

}
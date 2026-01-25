package com.mycodingtest.application.review.query;

import com.mycodingtest.domain.common.DomainPage;
import com.mycodingtest.domain.common.exception.ResourceNotFoundException;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.domain.review.ReviewStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <h3>리뷰 및 오답 노트 서비스 (ReviewQueryService)</h3>
 * <p>
 * 사용자의 학습 기록(Review)에 대한 상태 변경, 평가, 조회 등의 비즈니스 로직을 수행합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class ReviewQueryService {

    private final ReviewRepository reviewRepository;
    private final ProblemRepository problemRepository;

    /**
     * 단건 리뷰 정보를 조회합니다. (소유권 검증 포함)
     */
    @Transactional(readOnly = true)
    public ReviewInfo getReview(Long reviewId, Long userId) {
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
    private ReviewInfo getReviewAndValidateOwnership(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(ResourceNotFoundException::new);

        review.validateOwnership(userId);

        return ReviewInfo.from(review);
    }


    @Transactional(readOnly = true)
    public ReviewSummaryPage<ReviewSummary> getReviewSummaries(Long userId, int page, int size, ReviewStatus filter) {
        // 1. 리뷰 목록 페이징 조회
        DomainPage<Review> domainPage = reviewRepository.findAllByUserIdAndStatus(userId, filter, page, size);
        List<Review> reviews = domainPage.content();

        // 2. 연관된 ID 추출
        List<Long> problemIds = reviews.stream().map(Review::getProblemId).toList();

        // 3. 문제 정보들 한꺼번에 가져오기
        Map<Long, Problem> problemMap = problemRepository.findAllByIdIn(problemIds).stream()
                .collect(Collectors.toMap(Problem::getId, problem -> problem));

        // 5. 조립
        List<ReviewSummary> reviewSummaries = reviews.stream()
                .map(review -> ReviewSummary.from(problemMap.get(review.getProblemId()), review))
                .toList();
        return ReviewSummaryPage.from(reviewSummaries, domainPage);
    }

    @Transactional(readOnly = true)
    public ReviewSummaryPage<ReviewSummary> getAllReviewSummaries(Long userId, int page, int size) {
        DomainPage<Review> domainPage = reviewRepository.findAllByUserId(userId, page, size);
        List<Review> reviews = domainPage.content();

        // 2. 연관된 ID 추출
        List<Long> problemIds = reviews.stream().map(Review::getProblemId).toList();

        // 3. 문제 정보들 한꺼번에 가져오기
        Map<Long, Problem> problemMap = problemRepository.findAllByIdIn(problemIds).stream()
                .collect(Collectors.toMap(Problem::getId, problem -> problem));

        // 5. 조립
        List<ReviewSummary> reviewSummaries = reviews.stream()
                .map(review -> ReviewSummary.from(problemMap.get(review.getProblemId()), review))
                .toList();
        return ReviewSummaryPage.from(reviewSummaries, domainPage);
    }

    public ReviewSummaryPage<ReviewSummary> getFavoriteReviewSummary(Long userId, int page, int size) {
        DomainPage<Review> domainPage = reviewRepository.findAllByUserIdAndFavorite(userId, page, size);
        List<Review> reviews = domainPage.content();

        // 2. 연관된 ID 추출
        List<Long> problemIds = reviews.stream().map(Review::getProblemId).toList();

        // 3. 문제 정보들 한꺼번에 가져오기
        Map<Long, Problem> problemMap = problemRepository.findAllByIdIn(problemIds).stream()
                .collect(Collectors.toMap(Problem::getId, problem -> problem));

        // 5. 조립
        List<ReviewSummary> reviewSummaries = reviews.stream()
                .map(review -> ReviewSummary.from(problemMap.get(review.getProblemId()), review))
                .toList();
        return ReviewSummaryPage.from(reviewSummaries, domainPage);
    }
}
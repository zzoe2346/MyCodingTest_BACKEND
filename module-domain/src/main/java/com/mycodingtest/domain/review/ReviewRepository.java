package com.mycodingtest.domain.review;

import com.mycodingtest.domain.common.DomainPage;

import java.util.Optional;

/**
 * <h3>Review Repository Interface (Domain Layer)</h3>
 * <p>
 * {@link Review} 엔티티의 영속성을 관리하는 도메인 저장소 인터페이스입니다.
 * 기술 종속성을 제거하고 순수 도메인 로직에 필요한 메서드만 정의합니다.
 * </p>
 */
public interface ReviewRepository {

    /**
     * 리뷰를 저장합니다.
     *
     * @param review 저장할 리뷰 엔티티
     * @return 저장된 리뷰 엔티티
     */
    Review create(Review review);

    /**
     * 리뷰를 수정합니다.
     *
     * @param review 수정할 리뷰 엔티티
     * @return 수정된 리뷰 엔티티
     */
    Review update(Review review);

    /**
     * ID로 리뷰를 조회합니다.
     *
     * @param id 리뷰 ID
     * @return 리뷰 엔티티 (Optional)
     */
    Optional<Review> findById(Long id);

    /**
     * 특정 사용자의 '리뷰하지 않은(Pending)' 문제 개수를 카운트합니다.
     * <p>
     * 사용자에게 남은 과제(To-Do)가 얼마나 있는지 보여주는 대시보드 지표로 활용됩니다.
     * </p>
     *
     * @param userId 사용자 식별자
     * @return 리뷰 대기 중인 항목의 개수
     */
    long countPendingReviewsByUserId(Long userId);

    /**
     * 특정 문제에 대한 사용자의 리뷰를 조회합니다.
     * <p>
     * 문제당 사용자별로 하나의 리뷰만 존재합니다.
     * </p>
     *
     * @param problemId 문제 ID
     * @param userId    사용자 ID
     * @return 리뷰 엔티티 (Optional)
     */
    Optional<Review> findByProblemIdAndUserId(Long problemId, Long userId);

    /**
     * 사용자의 리뷰 목록을 상태별로 페이징 조회합니다.
     *
     * @param userId 사용자 ID
     * @param filter 리뷰 상태 필터
     * @param page   페이지 번호 (0부터 시작)
     * @param size   페이지당 크기
     * @return 페이징된 리뷰 목록
     */
    DomainPage<Review> findAllByUserIdAndStatus(Long userId, ReviewStatus filter, int page, int size);

    DomainPage<Review> findAllByUserId(Long userId, int page, int size);

    DomainPage<Review> findAllByUserIdAndFavorite(Long userId, int page, int size);
}
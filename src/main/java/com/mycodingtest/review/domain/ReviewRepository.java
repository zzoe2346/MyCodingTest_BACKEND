package com.mycodingtest.review.domain;

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
     * @param review 저장할 리뷰 엔티티
     * @return 저장된 리뷰 엔티티
     */
    Review save(Review review);

    /**
     * ID로 리뷰를 조회합니다.
     * @param id 리뷰 ID
     * @return 리뷰 엔티티 (Optional)
     */
    Optional<Review> findById(Long id);

    /**
     * 특정 사용자의 '리뷰하지 않은(Pending)' 문제 개수를 카운트합니다.
     * <p>사용자에게 남은 과제(To-Do)가 얼마나 있는지 보여주는 대시보드 지표로 활용됩니다.</p>
     * 
     * @param userId 사용자 식별자
     * @return 리뷰 대기 중인 항목의 개수
     */
    long countPendingReviewsByUserId(Long userId);

}
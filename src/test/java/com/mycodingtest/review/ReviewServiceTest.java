package com.mycodingtest.review;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.review.dto.ReviewRatingLevelsUpdateRequest;
import com.mycodingtest.review.dto.ReviewRecentStatusResponse;
import com.mycodingtest.review.dto.ReviewResponse;
import com.mycodingtest.review.dto.WaitReviewCountResponse;
import com.mycodingtest.storage.StorageService;
import com.mycodingtest.storage.dto.UrlResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@MockitoSettings
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private StorageService storageService;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    @DisplayName("체감 난이도와 재복습 필요도를 수정한다")
    void updateReviewRatingLevels() {
        // given
        ReviewRatingLevelsUpdateRequest request = new ReviewRatingLevelsUpdateRequest(3, 2);
        Long reviewId = 1L;
        Long userId = 1L;
        Review review = mock(Review.class);
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        reviewService.updateReviewRatingLevels(request, reviewId, userId);

        // then
        verify(review, times(1)).updateRatingLevels(request.difficultyLevel(), request.importanceLevel());
    }

    @Test
    @DisplayName("존재하지 않는 리뷰에 대한 수정 요청시 예외가 발생한다")
    void updateReviewRatingLevels_WhenReviewNotFound() {
        // given
        ReviewRatingLevelsUpdateRequest request = new ReviewRatingLevelsUpdateRequest(3, 2);
        Long reviewId = 999L;
        Long userId = 1L;

        given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

        // when then
        assertThrows(ResourceNotFoundException.class, () ->
                reviewService.updateReviewRatingLevels(request, reviewId, userId));
    }

    @Test
    @DisplayName("리뷰 정보를 조회한다")
    void getReview() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;

        Review review = mock(Review.class);
        review.completeReview();
        given(review.getDifficultyLevel()).willReturn(3);
        given(review.getImportanceLevel()).willReturn(3);
        given(review.isReviewed()).willReturn(true);
        given(review.getReviewedAt()).willReturn(LocalDateTime.now());
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        ReviewResponse response = reviewService.getReview(reviewId, userId);

        // then
        assertThat(response.reviewed()).isTrue();
    }

    @Test
    @DisplayName("메모 수정 URL을 조회한다")
    void getMemoUpdateUrl() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        String expectedUrl = "https://example.com/update";

        Review review = mock(Review.class);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
        given(storageService.getMemoUpdateUrl(String.valueOf(reviewId), userId)).willReturn(expectedUrl);

        // when
        UrlResponse response = reviewService.getMemoUpdateUrl(reviewId, userId);

        // then
        assertThat(response.url()).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("메모 읽기 URL을 조회한다")
    void getMemoReadUrl() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;
        String expectedUrl = "https://example.com/read";

        Review review = mock(Review.class);

        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));
        given(storageService.getMemoReadUrl(String.valueOf(reviewId), userId)).willReturn(expectedUrl);

        // when
        UrlResponse response = reviewService.getMemoReadUrl(reviewId, userId);

        // then
        assertThat(response.url()).isEqualTo(expectedUrl);
    }

    @Test
    @DisplayName("리뷰 상태를 완료로 전환한다")
    void updateReviewStatus() {
        // given
        Long reviewId = 1L;
        Long userId = 1L;

        Review review = mock(Review.class);
        given(review.getReviewedAt()).willReturn(LocalDateTime.now());
        given(review.isReviewed()).willReturn(true);
        given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

        // when
        ReviewRecentStatusResponse response = reviewService.updateReviewStatus(reviewId, userId);

        // then
        assertThat(response.reviewed()).isTrue();
        assertThat(response.reviewedAt()).isNotNull();
    }

    @Test
    @DisplayName("리뷰를 기다리는 문제 개수를 조회한다")
    void getWaitReviewCount() {
        // given
        Long userId = 1L;
        int expectedCount = 5;

        given(reviewRepository.countAllByReviewedIsFalseAndUserId(userId)).willReturn(expectedCount);

        // when
        WaitReviewCountResponse response = reviewService.getWaitReviewCount(userId);

        // then
        assertThat(response.count()).isEqualTo(expectedCount);
    }
}
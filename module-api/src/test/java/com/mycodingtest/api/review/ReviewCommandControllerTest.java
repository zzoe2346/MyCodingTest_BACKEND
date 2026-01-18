package com.mycodingtest.api.review;

import com.mycodingtest.api.review.dto.request.UpdateReviewRequest;
import com.mycodingtest.application.review.ReviewService;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewStatus;
import com.mycodingtest.security.CustomUserDetails;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewCommandControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewCommandController controller;

    @Nested
    class 리뷰_수정 {

        @Test
        void 리뷰를_수정하면_OK와_수정된_상태를_반환한다() {
            // given
            Long reviewId = 1L;
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");

            UpdateReviewRequest request = new UpdateReviewRequest(
                    true,
                    5,
                    4,
                    "updated code",
                    "updated content",
                    ReviewStatus.COMPLETED);

            Review updatedReview = Review.from(
                    reviewId,
                    1000L,
                    userId,
                    "updated content",
                    5,
                    4,
                    "updated code",
                    null,
                    ReviewStatus.COMPLETED,
                    true,
                    true,
                    null,
                    null);
            given(reviewService.updateReview(any())).willReturn(updatedReview);

            // when
            var result = controller.updateReview(reviewId, request, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().status()).isEqualTo(ReviewStatus.COMPLETED);
        }

        @Test
        void 일부_필드만_수정해도_동작한다() {
            // given
            Long reviewId = 1L;
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");

            UpdateReviewRequest request = new UpdateReviewRequest(
                    true,
                    null,
                    null,
                    null,
                    null,
                    null);

            Review updatedReview = Review.from(
                    reviewId,
                    null,
                    userId,
                    null,
                    -1,
                    -1,
                    null,
                    null,
                    ReviewStatus.TO_DO,
                    true,
                    true,
                    null,
                    null);
            given(reviewService.updateReview(any())).willReturn(updatedReview);

            // when
            var result = controller.updateReview(reviewId, request, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody().isFavorite()).isTrue();
        }
    }
}

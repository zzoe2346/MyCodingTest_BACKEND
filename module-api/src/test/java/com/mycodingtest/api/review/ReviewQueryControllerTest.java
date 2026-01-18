package com.mycodingtest.api.review;

import com.mycodingtest.application.review.ReviewService;
import com.mycodingtest.application.review.dto.PagedResult;
import com.mycodingtest.application.review.dto.ReviewSummary;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ReviewQueryControllerTest {

    @Mock
    private ReviewService reviewService;

    @InjectMocks
    private ReviewQueryController controller;

    @Nested
    class 리뷰_단건_조회 {

        @Test
        void 리뷰를_조회하면_OK와_리뷰정보를_반환한다() {
            // given
            Long reviewId = 1L;
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");

            Review review = Review.from(
                    reviewId,
                    1000L,
                    userId,
                    "테스트 리뷰",
                    3,
                    4,
                    null,
                    null,
                    ReviewStatus.IN_PROGRESS,
                    true,
                    true,
                    null,
                    null);
            given(reviewService.getReview(reviewId, userId)).willReturn(review);

            // when
            var result = controller.getReview(reviewId, userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().difficultyLevel()).isEqualTo(3);
            assertThat(result.getBody().importanceLevel()).isEqualTo(4);
        }
    }

    @Nested
    class 대기중_리뷰_개수_조회 {

        @Test
        void TO_DO_상태_리뷰_개수를_반환한다() {
            // given
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");
            given(reviewService.getReviewCountStatusInToDo(userId)).willReturn(5L);

            // when
            var result = controller.getWaitReviewCount(userDetails);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
            assertThat(result.getBody().count()).isEqualTo(5L);
        }
    }

    @Nested
    class 리뷰_목록_페이징_조회 {

        @Test
        void 페이징된_리뷰_목록을_반환한다() {
            // given
            Long userId = 1L;
            CustomUserDetails userDetails = new CustomUserDetails(userId, "pic", "user");
            int page = 0;
            int size = 10;
            ReviewStatus filter = ReviewStatus.TO_DO;

            PagedResult<ReviewSummary> pagedResult = new PagedResult<>(
                    List.of(),
                    0,
                    0,
                    0,
                    10,
                    true);
            given(reviewService.getReviewSummaries(userId, page, size, filter)).willReturn(pagedResult);

            // when
            var result = controller.getReviewSummaries(userDetails, page, size, filter);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(result.getBody()).isNotNull();
        }
    }
}

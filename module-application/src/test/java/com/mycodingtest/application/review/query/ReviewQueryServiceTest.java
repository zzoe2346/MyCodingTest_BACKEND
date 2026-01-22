package com.mycodingtest.application.review.query;

import com.mycodingtest.application.review.command.ReviewCommandService;
import com.mycodingtest.domain.common.exception.InvalidOwnershipException;
import com.mycodingtest.domain.common.exception.ResourceNotFoundException;
import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@MockitoSettings
class ReviewQueryServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ReviewQueryService reviewQueryService;

    @Nested
    class 리뷰_단건_조회 {

        @Test
        void 리뷰_소유자가_조회하면_리뷰를_반환한다() {
            // given
            Long reviewId = 1L;
            Long userId = 100L;
            Review review = Review.builder()
                    .id(reviewId)
                    .userId(userId)
                    .content("테스트 리뷰")
                    .build();
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

            // when
            ReviewInfo result = reviewQueryService.getReview(reviewId, userId);

            // then
            assertThat(result.id()).isEqualTo(reviewId);
            assertThat(result.content()).isEqualTo("테스트 리뷰");
        }

        @Test
        void 리뷰가_존재하지_않으면_예외가_발생한다() {
            // given
            Long reviewId = 999L;
            Long userId = 100L;
            given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewQueryService.getReview(reviewId, userId))
                    .isInstanceOf(ResourceNotFoundException.class);
        }

        @Test
        void 소유자가_아닌_사용자가_조회하면_예외가_발생한다() {
            // given
            Long reviewId = 1L;
            Long ownerId = 100L;
            Long otherUserId = 999L;
            Review review = Review.builder()
                    .id(reviewId)
                    .userId(ownerId)
                    .build();
            given(reviewRepository.findById(reviewId)).willReturn(Optional.of(review));

            // when & then
            assertThatThrownBy(() -> reviewQueryService.getReview(reviewId, otherUserId))
                    .isInstanceOf(InvalidOwnershipException.class);
        }
    }



    @Nested
    class 대기중_리뷰_개수_조회 {

        @Test
        void 사용자의_대기중_리뷰_개수를_반환한다() {
            // given
            Long userId = 1L;
            given(reviewRepository.countPendingReviewsByUserId(userId)).willReturn(5L);

            // when
            long count = reviewQueryService.getReviewCountStatusInToDo(userId);

            // then
            assertThat(count).isEqualTo(5L);
        }
    }

}
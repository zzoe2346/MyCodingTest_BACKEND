package com.mycodingtest.application.review;

import com.mycodingtest.application.review.dto.CreateReviewCommand;
import com.mycodingtest.application.review.dto.UpdateReviewCommand;
import com.mycodingtest.domain.common.exception.InvalidOwnershipException;
import com.mycodingtest.domain.common.exception.ResourceNotFoundException;
import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.domain.review.ReviewStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ReviewService reviewService;

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
            Review result = reviewService.getReview(reviewId, userId);

            // then
            assertThat(result.getId()).isEqualTo(reviewId);
            assertThat(result.getContent()).isEqualTo("테스트 리뷰");
        }

        @Test
        void 리뷰가_존재하지_않으면_예외가_발생한다() {
            // given
            Long reviewId = 999L;
            Long userId = 100L;
            given(reviewRepository.findById(reviewId)).willReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> reviewService.getReview(reviewId, userId))
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
            assertThatThrownBy(() -> reviewService.getReview(reviewId, otherUserId))
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
            long count = reviewService.getReviewCountStatusInToDo(userId);

            // then
            assertThat(count).isEqualTo(5L);
        }
    }

    @Nested
    class 리뷰_생성 {

        @Test
        void 새로운_리뷰를_생성한다() {
            // given
            LocalDateTime submittedAt = LocalDateTime.of(2026, 1, 17, 12, 0, 0);
            CreateReviewCommand command = CreateReviewCommand.builder()
                    .problemId(1000L)
                    .userId(1L)
                    .sourceCode("public class Main {}")
                    .submittedAt(submittedAt)
                    .resultText("맞았습니다!!")
                    .build();
            given(reviewRepository.findByProblemIdAndUserId(1000L, 1L)).willReturn(Optional.empty());

            // when
            reviewService.createReview(command);

            // then
            ArgumentCaptor<Review> captor = ArgumentCaptor.forClass(Review.class);
            verify(reviewRepository).create(captor.capture());

            Review capturedReview = captor.getValue();
            assertThat(capturedReview.getProblemId()).isEqualTo(1000L);
            assertThat(capturedReview.getUserId()).isEqualTo(1L);
            assertThat(capturedReview.getRevisedCode()).isEqualTo("public class Main {}");
        }

        @Test
        void 기존_리뷰가_있으면_상태를_업데이트하고_새_리뷰도_생성한다() {
            // given
            LocalDateTime submittedAt = LocalDateTime.of(2026, 1, 17, 12, 0, 0);
            CreateReviewCommand command = CreateReviewCommand.builder()
                    .problemId(1000L)
                    .userId(1L)
                    .sourceCode("public class Main {}")
                    .submittedAt(submittedAt)
                    .resultText("맞았습니다!!")
                    .build();
            Review existingReview = Review.builder()
                    .id(1L)
                    .problemId(1000L)
                    .userId(1L)
                    .status(ReviewStatus.COMPLETED)
                    .reviewed(true)
                    .build();
            given(reviewRepository.findByProblemIdAndUserId(1000L, 1L)).willReturn(Optional.of(existingReview));

            // when
            reviewService.createReview(command);

            // then
            verify(reviewRepository).update(existingReview);
            verify(reviewRepository).create(any(Review.class));
            assertThat(existingReview.getStatus()).isEqualTo(ReviewStatus.TO_DO);
        }
    }

    @Nested
    class 리뷰_수정 {

        @Test
        void 리뷰_필드를_업데이트한다() {
            // given
            Review existingReview = Review.builder()
                    .id(1L)
                    .userId(1L)
                    .favorited(false)
                    .difficultyLevel(1)
                    .importanceLevel(1)
                    .status(ReviewStatus.TO_DO)
                    .build();
            given(reviewRepository.findById(1L)).willReturn(Optional.of(existingReview));

            UpdateReviewCommand command = new UpdateReviewCommand(
                    1L, 1L, true, 5, 4, "new code", "new content", ReviewStatus.IN_PROGRESS);

            // when
            Review result = reviewService.updateReview(command);

            // then
            assertThat(result.isFavorited()).isTrue();
            assertThat(result.getDifficultyLevel()).isEqualTo(5);
            assertThat(result.getImportanceLevel()).isEqualTo(4);
            assertThat(result.getRevisedCode()).isEqualTo("new code");
            assertThat(result.getContent()).isEqualTo("new content");
            assertThat(result.getStatus()).isEqualTo(ReviewStatus.IN_PROGRESS);
        }
    }
}

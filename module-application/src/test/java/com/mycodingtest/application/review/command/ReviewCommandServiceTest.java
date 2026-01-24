package com.mycodingtest.application.review.command;

import com.mycodingtest.domain.problem.ProblemRepository;
import com.mycodingtest.domain.review.Review;
import com.mycodingtest.domain.review.ReviewRepository;
import com.mycodingtest.domain.review.ReviewStatus;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@MockitoSettings
class ReviewCommandServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ReviewCommandService reviewCommandService;


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
            reviewCommandService.createReview(command);

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
            reviewCommandService.createReview(command);

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

            UpdateReviewCommand command = UpdateReviewCommand.from(
                    1L,
                    1L,
                    true,
                    5,
                    4,
                    "new code",
                    "new content",
                    ReviewStatus.IN_PROGRESS);

            // when
            UpdateReviewResult result = reviewCommandService.updateReview(command);

            // then
            assertThat(result.isFavorited()).isTrue();
            assertThat(result.difficultyLevel()).isEqualTo(5);
            assertThat(result.importanceLevel()).isEqualTo(4);
            assertThat(result.revisedCode()).isEqualTo("new code");
            assertThat(result.content()).isEqualTo("new content");
            assertThat(result.status()).isEqualTo(ReviewStatus.IN_PROGRESS);
        }
    }

}
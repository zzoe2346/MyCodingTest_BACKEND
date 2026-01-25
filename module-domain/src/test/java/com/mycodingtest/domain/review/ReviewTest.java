package com.mycodingtest.domain.review;

import com.mycodingtest.domain.common.exception.InvalidOwnershipException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReviewTest {

    @Nested
    class 소유권_검증 {

        @Test
        void 리뷰_소유자가_요청하면_예외가_발생하지_않는다() {
            // given
            Long ownerId = 1L;
            Review review = Review.from(
                    100L,
                    null,
                    ownerId,
                    null,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    false, true,
                    null,
                    null);

            // when & then
            review.validateOwnership(ownerId);
            // 예외가 발생하지 않으면 성공
        }

        @Test
        void 리뷰_소유자가_아닌_사용자가_요청하면_예외가_발생한다() {
            // given
            Long ownerId = 1L;
            Long otherUserId = 999L;
            Review review = Review.from(
                    100L,
                    null,
                    ownerId,
                    null,
                    -1,
                    -1,
                    null,
                    null,
                    null,
                    false, true,
                    null,
                    null);

            // when & then
            assertThatThrownBy(() -> review.validateOwnership(otherUserId))
                    .isInstanceOf(InvalidOwnershipException.class);
        }
    }

    @Nested
    class 리뷰_수정 {

        @Test
        void 모든_필드가_null이면_기존_값이_유지된다() {
            // given
            Review review = Review.from(
                    1L,
                    null,
                    1L,
                    "original content",
                    3,
                    4,
                    "original code",
                    null,
                    ReviewStatus.IN_PROGRESS,
                    true,
                    true,
                    null,
                    null);

            // when
            review.update(null, null, null, null, null, null);

            // then
            assertThat(review.isFavorited()).isTrue();
            assertThat(review.getDifficultyLevel()).isEqualTo(3);
            assertThat(review.getImportanceLevel()).isEqualTo(4);
            assertThat(review.getRevisedCode()).isEqualTo("original code");
            assertThat(review.getContent()).isEqualTo("original content");
            assertThat(review.getStatus()).isEqualTo(ReviewStatus.IN_PROGRESS);
        }

        @Test
        void 전달된_필드만_업데이트된다() {
            // given
            Review review = Review.from(
                    1L,
                    null,
                    1L,
                    "old content",
                    1,
                    1,
                    "old code",
                    null,
                    ReviewStatus.TO_DO,
                    false,
                    true,
                    null,
                    null);

            // when
            review.update(true, 5, null, "new code", null, ReviewStatus.IN_PROGRESS);

            // then
            assertThat(review.isFavorited()).isTrue();
            assertThat(review.getDifficultyLevel()).isEqualTo(5);
            assertThat(review.getImportanceLevel()).isEqualTo(1); // 변경 안됨
            assertThat(review.getRevisedCode()).isEqualTo("new code");
            assertThat(review.getContent()).isEqualTo("old content"); // 변경 안됨
            assertThat(review.getStatus()).isEqualTo(ReviewStatus.IN_PROGRESS);
        }

        @Test
        void 상태가_COMPLETED로_변경되면_reviewedAt이_설정된다() {
            // given
            Review review = Review.from(
                    1L, null, 1L, null,
                    -1, -1, null, null,
                    ReviewStatus.IN_PROGRESS, false, true, null, null);
            LocalDateTime beforeUpdate = LocalDateTime.now();

            // when
            review.update(null, null, null, null, null, ReviewStatus.COMPLETED);

            // then
            assertThat(review.getStatus()).isEqualTo(ReviewStatus.COMPLETED);
            assertThat(review.getReviewedAt()).isNotNull();
            assertThat(review.getReviewedAt()).isAfterOrEqualTo(beforeUpdate);
        }

        @Test
        void COMPLETED가_아닌_상태로_변경하면_reviewedAt이_설정되지_않는다() {
            // given
            Review review = Review.from(
                    1L, null, 1L, null,
                    -1, -1, null, null,
                    ReviewStatus.TO_DO, false, true, null, null);

            // when
            review.update(null, null, null, null, null, ReviewStatus.IN_PROGRESS);

            // then
            assertThat(review.getStatus()).isEqualTo(ReviewStatus.IN_PROGRESS);
            assertThat(review.getReviewedAt()).isNull();
        }
    }

    @Nested
    class 채점_생성_이벤트_처리 {

        @Test
        void 새로운_채점이_생성되면_리뷰_상태가_초기화된다() {
            // given
            Review review = Review.from(
                    1L, null, 1L, null,
                    -1, -1, null, null,
                    ReviewStatus.COMPLETED, false, true, null, null);
            LocalDateTime submitTime = LocalDateTime.of(2026, 1, 17, 12, 0, 0);
            String result = "맞았습니다!!";

            // when
            review.onJudgmentCreated(submitTime, result);

            // then
            assertThat(review.getReviewed()).isFalse();
            assertThat(review.getStatus()).isEqualTo(ReviewStatus.TO_DO);
            assertThat(review.getRecentSubmitAt()).isEqualTo(submitTime);
            assertThat(review.getRecentResult()).isEqualTo(result);
        }
    }

    @Nested
    class 리뷰_생성 {

        @Test
        void 정적_팩토리_메서드로_새_리뷰를_생성할_수_있다() {
            // given
            Long problemId = 1000L;
            Long userId = 1L;
            String sourceCode = "code";
            LocalDateTime submittedAt = LocalDateTime.now();
            String resultText = "맞았습니다!!";

            // when
            Review review = Review.from(problemId, userId, sourceCode, submittedAt, resultText);

            // then
            assertThat(review.getProblemId()).isEqualTo(problemId);
            assertThat(review.getUserId()).isEqualTo(userId);
            assertThat(review.getRevisedCode()).isEqualTo(sourceCode);
            assertThat(review.getStatus()).isEqualTo(ReviewStatus.TO_DO);
            assertThat(review.getReviewed()).isFalse();
        }

        @Test
        void 필수_필드가_없으면_예외가_발생한다() {
            // given & when & then
            assertThatThrownBy(() -> Review.from(1L, null, "code", null, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("사용자 ID는 필수입니다");
        }
    }
}

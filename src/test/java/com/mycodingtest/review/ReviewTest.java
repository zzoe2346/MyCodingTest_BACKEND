package com.mycodingtest.review;

import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.user.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Tag("unit")
class ReviewTest {

    @Test
    @DisplayName("리뷰 객체가 생성되면 기본값이 설정된다")
    void createReview() {
        // given
        User user = new User("name", "email", "picture", "provider", "oauthId");

        // when
        Review review = new Review(user);

        // then
        assertThat(review.getId()).isNull();
        assertThat(review.getUser()).isEqualTo(user);
        assertThat(review.getDifficultyLevel()).isEqualTo(-1);
        assertThat(review.getImportanceLevel()).isEqualTo(-1);
        assertThat(review.isReviewed()).isFalse();
        assertThat(review.getReviewedAt()).isNull();
    }

    @Test
    @DisplayName("난이도와 중요도 수준을 업데이트한다")
    void updateRatingLevels() {
        // given
        User user = new User("name", "email", "picture", "provider", "oauthId");
        Review review = new Review(user);

        // when
        review.updateRatingLevels(3, 4);

        // then
        assertThat(review.getDifficultyLevel()).isEqualTo(3);
        assertThat(review.getImportanceLevel()).isEqualTo(4);
    }

    @Test
    @DisplayName("소유권 검증을 통과한다")
    void validateOwnership_Success() {
        // given
        User user = mock(User.class);
        Review review = new Review(user);
        when(user.getId()).thenReturn(1L);

        // when then
        review.validateOwnership(1L);
    }

    @Test
    @DisplayName("소유권 검증에 실패하면 예외가 발생한다")
    void validateOwnership_Fail() {
        // given
        User user = mock(User.class);
        Review review = new Review(user);
        when(user.getId()).thenReturn(999L);

        // when then
        assertThrows(InvalidOwnershipException.class, () -> review.validateOwnership(2L));
    }

    @Test
    @DisplayName("리뷰를 완료 상태로 변경한다")
    void completeReview() {
        // given
        User user = new User("name", "email", "picture", "provider", "oauthId");
        Review review = new Review(user);
        LocalDateTime before = LocalDateTime.now();

        // when
        review.completeReview();

        // then
        assertThat(review.isReviewed()).isTrue();
        assertThat(review.getReviewedAt()).isNotNull();
        assertThat(review.getReviewedAt()).isAfterOrEqualTo(before);
    }
}
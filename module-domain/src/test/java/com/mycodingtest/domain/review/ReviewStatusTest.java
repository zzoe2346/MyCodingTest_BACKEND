package com.mycodingtest.domain.review;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewStatusTest {

    @Test
    void 리뷰_상태는_4가지가_존재한다() {
        // given
        ReviewStatus[] statuses = ReviewStatus.values();

        // when & then
        assertThat(statuses).hasSize(4);
        assertThat(statuses).containsExactly(
                ReviewStatus.TO_DO,
                ReviewStatus.IN_PROGRESS,
                ReviewStatus.COMPLETED,
                ReviewStatus.MASTERED);
    }

    @Test
    void 문자열로_리뷰_상태를_조회할_수_있다() {
        // given
        String statusName = "IN_PROGRESS";

        // when
        ReviewStatus status = ReviewStatus.valueOf(statusName);

        // then
        assertThat(status).isEqualTo(ReviewStatus.IN_PROGRESS);
    }

    @Test
    void 각_상태의_이름이_올바르다() {
        // given & when & then
        assertThat(ReviewStatus.TO_DO.name()).isEqualTo("TO_DO");
        assertThat(ReviewStatus.IN_PROGRESS.name()).isEqualTo("IN_PROGRESS");
        assertThat(ReviewStatus.COMPLETED.name()).isEqualTo("COMPLETED");
        assertThat(ReviewStatus.MASTERED.name()).isEqualTo("MASTERED");
    }
}

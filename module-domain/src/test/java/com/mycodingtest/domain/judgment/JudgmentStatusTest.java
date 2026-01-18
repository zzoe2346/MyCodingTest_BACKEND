package com.mycodingtest.domain.judgment;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JudgmentStatusTest {

    @Test
    void 채점_상태는_2가지가_존재한다() {
        // given
        JudgmentStatus[] statuses = JudgmentStatus.values();

        // when & then
        assertThat(statuses).hasSize(3);
        assertThat(statuses).containsExactly(
                JudgmentStatus.SUCCESS,
                JudgmentStatus.FAIL,
                JudgmentStatus.ERROR);
    }

    @Test
    void 문자열로_채점_상태를_조회할_수_있다() {
        // given
        String statusName = "SUCCESS";

        // when
        JudgmentStatus status = JudgmentStatus.valueOf(statusName);

        // then
        assertThat(status).isEqualTo(JudgmentStatus.SUCCESS);
    }

    @Test
    void 각_상태의_이름이_올바르다() {
        // given & when & then
        assertThat(JudgmentStatus.SUCCESS.name()).isEqualTo("SUCCESS");
        assertThat(JudgmentStatus.FAIL.name()).isEqualTo("FAIL");
        assertThat(JudgmentStatus.ERROR.name()).isEqualTo("ERROR");
    }
}

package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JudgmentTest {

    @Nested
    class 채점_생성 {

        @Test
        void 필수_필드가_없으면_예외가_발생한다() {
            // given & when & then
            SubmissionInfo submissionInfo = SubmissionInfo.from(123L, Platform.BOJ, JudgmentStatus.SUCCESS, null, "code");

            assertThatThrownBy(
                    () -> Judgment.from(1L, null, submissionInfo))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("사용자 ID는 필수입니다");

            assertThatThrownBy(() -> Judgment.from(1L, 1L, null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("제출 정보는 필수입니다");
        }
    }
}

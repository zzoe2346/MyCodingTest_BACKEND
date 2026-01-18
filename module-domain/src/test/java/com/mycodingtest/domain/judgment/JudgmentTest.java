package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JudgmentTest {

    @Nested
    class 채점_생성 {

        @Test
        void 정적_팩토리_메서드로_채점_객체를_생성할_수_있다() {
            // given
            Long problemId = 1000L;
            Long userId = 1L;
            Long submissionId = 99999L;
            String sourceCode = "public class Main {}";

            // when
            Judgment judgment = Judgment.from(
                    problemId,
                    userId,
                    submissionId,
                    JudgmentStatus.SUCCESS,
                    Platform.BOJ,
                    null,
                    sourceCode);

            // then
            assertThat(judgment.getProblemId()).isEqualTo(problemId);
            assertThat(judgment.getUserId()).isEqualTo(userId);
            assertThat(judgment.getSubmissionId()).isEqualTo(submissionId);
            assertThat(judgment.getStatus()).isEqualTo(JudgmentStatus.SUCCESS);
            assertThat(judgment.getPlatform()).isEqualTo(Platform.BOJ);
            assertThat(judgment.getSourceCode()).isEqualTo(sourceCode);
        }

        @Test
        void 메타데이터를_포함하여_채점_객체를_생성할_수_있다() {
            // given
            BojMetaData metaData = BojMetaData.builder()
                    .submissionId(12345L)
                    .baekjoonId("testUser")
                    .resultText("맞았습니다!!")
                    .memory(1024)
                    .time(50)
                    .language("Java 11")
                    .codeLength(200)
                    .build();

            // when
            Judgment judgment = Judgment.from(
                    1000L,
                    1L,
                    12345L,
                    JudgmentStatus.SUCCESS,
                    Platform.BOJ, metaData,
                    "code");

            // then
            assertThat(judgment.getMetaData()).isNotNull();
            assertThat(judgment.getMetaData()).isInstanceOf(BojMetaData.class);

            BojMetaData retrievedMetaData = (BojMetaData) judgment.getMetaData();
            assertThat(retrievedMetaData.getBaekjoonId()).isEqualTo("testUser");
            assertThat(retrievedMetaData.getMemory()).isEqualTo(1024);
        }

        @Test
        void 필수_필드가_없으면_예외가_발생한다() {
            // given & when & then
            assertThatThrownBy(
                    () -> Judgment.from(null, 1L, 123L, JudgmentStatus.SUCCESS, Platform.BOJ, null, "code"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("문제 ID는 필수입니다");

            assertThatThrownBy(
                    () -> Judgment.from(1L, null, 123L, JudgmentStatus.SUCCESS, Platform.BOJ, null, "code"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("사용자 ID는 필수입니다");

            assertThatThrownBy(() -> Judgment.from(1L, 1L, null, JudgmentStatus.SUCCESS, Platform.BOJ, null, "code"))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("제출 ID는 필수입니다");
        }
    }
}

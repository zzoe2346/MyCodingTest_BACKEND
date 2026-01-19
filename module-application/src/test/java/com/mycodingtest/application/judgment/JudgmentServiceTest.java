package com.mycodingtest.application.judgment;

import com.mycodingtest.application.judgment.dto.CreateBojJudgmentCommand;
import com.mycodingtest.application.judgment.dto.DeleteJudgmentCommand;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentRepository;
import com.mycodingtest.domain.judgment.JudgmentStatus;
import com.mycodingtest.domain.judgment.SubmissionInfo;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JudgmentServiceTest {

    @Mock
    private JudgmentRepository repository;

    @InjectMocks
    private JudgmentService judgmentService;

    @Nested
    class 채점_결과_조회 {

        @Test
        void 문제ID와_사용자ID로_채점_목록을_조회한다() {
            // given
            Long problemId = 1000L;
            Long userId = 1L;
            SubmissionInfo info1 = SubmissionInfo.from(123L, Platform.BOJ, JudgmentStatus.SUCCESS, null, "code1");
            SubmissionInfo info2 = SubmissionInfo.from(124L, Platform.BOJ, JudgmentStatus.FAIL, null, "code2");

            List<Judgment> expectedJudgments = List.of(
                    Judgment.from(problemId, userId, info1),
                    Judgment.from(problemId, userId, info2));
            given(repository.findByProblemIdAndUserId(problemId, userId)).willReturn(expectedJudgments);

            // when
            List<Judgment> result = judgmentService.getJudgments(problemId, userId);

            // then
            assertThat(result).hasSize(2);
            assertThat(result).isEqualTo(expectedJudgments);
            verify(repository).findByProblemIdAndUserId(problemId, userId);
        }
    }

    @Nested
    class 채점_존재_확인 {

        @Test
        void 제출ID가_이미_존재하면_true를_반환한다() {
            // given
            Long submissionId = 99999L;
            Platform platform = Platform.BOJ;
            given(repository.existsBySubmissionId(submissionId, platform)).willReturn(true);

            // when
            boolean result = judgmentService.isJudgmentExist(submissionId, Platform.BOJ);

            // then
            assertThat(result).isTrue();
        }

        @Test
        void 제출ID가_존재하지_않으면_false를_반환한다() {
            // given
            Long submissionId = 99999L;
            Platform platform = Platform.BOJ;
            given(repository.existsBySubmissionId(submissionId, platform)).willReturn(false);

            // when
            boolean result = judgmentService.isJudgmentExist(submissionId, Platform.BOJ);

            // then
            assertThat(result).isFalse();
        }
    }

    @Nested
    class 백준_채점_생성 {

        @Test
        void Judgment를_생성하고_저장한다() {
            // given
            LocalDateTime submittedAt = LocalDateTime.of(2026, 1, 17, 10, 0, 0);
            CreateBojJudgmentCommand command = CreateBojJudgmentCommand.builder()
                    .submissionId(12345L)
                    .baekjoonId("testUser")
                    .resultText("맞았습니다!!")
                    .memory(1024)
                    .time(100)
                    .language("Java 11")
                    .codeLength(500)
                    .submittedAt(submittedAt)
                    .sourceCode("public class Main {}")
                    .problemId(1000L)
                    .userId(1L)
                    .build();

            SubmissionInfo submissionInfo = SubmissionInfo.from(12345L, Platform.BOJ, JudgmentStatus.SUCCESS, null, "public class Main {}");

            Judgment savedJudgment = Judgment.builder()
                    .id(1L)
                    .problemId(1000L)
                    .userId(1L)
                    .submissionInfo(submissionInfo)
                    .build();

            given(repository.save(any(Judgment.class))).willReturn(savedJudgment);

            // when
            Judgment result = judgmentService.createJudgmentFromBoj(command);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getSubmissionInfo().getSubmissionId()).isEqualTo(12345L);

            ArgumentCaptor<Judgment> captor = ArgumentCaptor.forClass(Judgment.class);
            verify(repository).save(captor.capture());

            Judgment capturedJudgment = captor.getValue();
            assertThat(capturedJudgment.getSubmissionInfo().getPlatform()).isEqualTo(Platform.BOJ);
            assertThat(capturedJudgment.getSubmissionInfo().getSourceCode()).isEqualTo("public class Main {}");
            assertThat(capturedJudgment.getSubmissionInfo().getMetaData()).isInstanceOf(BojMetaData.class);
        }
    }

    @Nested
    class 채점_삭제 {

        @Test
        void Command로_채점을_삭제한다() {
            // given
            DeleteJudgmentCommand command = new DeleteJudgmentCommand(100L, 1L);

            // when
            judgmentService.deleteJudgment(command);

            // then
            verify(repository).deleteByIdAndUserId(100L, 1L);
        }
    }
}

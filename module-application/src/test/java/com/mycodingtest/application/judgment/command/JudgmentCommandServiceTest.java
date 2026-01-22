package com.mycodingtest.application.judgment.command;

import com.mycodingtest.domain.judgment.JudgmentRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JudgmentCommandServiceTest {

    @Mock
    private JudgmentRepository repository;

    @InjectMocks
    private JudgmentCommandService judgmentCommandService;

    @Nested
    class 백준_채점_생성 {

        //TODO 테스트 코드 작성
        @Test
        void Judgment를_생성하고_저장한다() {
            // given
//            LocalDateTime submittedAt = LocalDateTime.of(2026, 1, 17, 10, 0, 0);
//            CreateBojJudgmentCommand command = CreateBojJudgmentCommand.builder()
//                    .submissionId(12345L)
//                    .baekjoonId("testUser")
//                    .resultText("맞았습니다!!")
//                    .memory(1024)
//                    .time(100)
//                    .language("Java 11")
//                    .codeLength(500)
//                    .submittedAt(submittedAt)
//                    .sourceCode("public class Main {}")
//                    .problemId(1000L)
//                    .userId(1L)
//                    .build();
//
//            SubmissionInfo submissionInfo = SubmissionInfo.from(12345L, Platform.BOJ, JudgmentStatus.SUCCESS, null, "public class Main {}");
//
//            Judgment savedJudgment = Judgment.builder()
//                    .id(1L)
//                    .problemId(1000L)
//                    .userId(1L)
//                    .submissionInfo(submissionInfo)
//                    .build();
//
//            given(repository.save(any(Judgment.class))).willReturn(savedJudgment);
//
//            // when
//            judgmentCommandService.createJudgmentFromBoj(command);
//
//            // then
//            assertThat(result.getId()).isEqualTo(1L);
//            assertThat(result.getSubmissionInfo().getSubmissionId()).isEqualTo(12345L);
//
//            ArgumentCaptor<Judgment> captor = ArgumentCaptor.forClass(Judgment.class);
//            verify(repository).save(captor.capture());
//
//            Judgment capturedJudgment = captor.getValue();
//            assertThat(capturedJudgment.getSubmissionInfo().getPlatform()).isEqualTo(Platform.BOJ);
//            assertThat(capturedJudgment.getSubmissionInfo().getSourceCode()).isEqualTo("public class Main {}");
//            assertThat(capturedJudgment.getSubmissionInfo().getMetaData()).isInstanceOf(BojMetaData.class);
        }
    }

    @Nested
    class 채점_삭제 {

        @Test
        void Command로_채점을_삭제한다() {
            // given
            DeleteJudgmentCommand command = new DeleteJudgmentCommand(100L, 1L);

            // when
            judgmentCommandService.deleteJudgment(command);

            // then
            verify(repository).deleteByIdAndUserId(100L, 1L);
        }
    }
}

package com.mycodingtest.application.judgment.command;

import com.mycodingtest.application.judgment.support.BojJudgmentAssembler;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.*;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class JudgmentCommandServiceTest {

    @Mock
    private JudgmentRepository repository;
    @Mock
    private BojJudgmentAssembler bojJudgmentAssembler;

    @InjectMocks
    private JudgmentCommandService judgmentCommandService;

    @Nested
    class 백준_채점_생성 {

        //TODO 테스트 코드 작성
        @Test
        void Judgment를_생성하고_저장한다() {
//             given
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
            BojMetaData metaData = BojMetaData.builder()
                    .baekjoonId(command.baekjoonId())
                    .codeLength(command.codeLength())
                    .language(command.language())
                    .resultText(command.resultText())
                    .memory(command.memory())
                    .time(command.time())
                    .submissionId(command.submissionId())
                    .submittedAt(command.submittedAt())
                    .build();

            SubmissionInfo submissionInfo = SubmissionInfo.from(command.submissionId(), Platform.BOJ, JudgmentStatus.SUCCESS, metaData, command.sourceCode());

            Judgment fromAssembler = Judgment.builder()
                    .problemId(command.problemId())
                    .userId(1L)
                    .submissionInfo(submissionInfo)
                    .build();

            Judgment savedJudgment = Judgment.builder()
                    .id(1L)
                    .problemId(command.problemId())
                    .userId(1L)
                    .submissionInfo(submissionInfo)
                    .build();

            given(bojJudgmentAssembler.assemble(command)).willReturn(fromAssembler);
            given(repository.save(any(Judgment.class))).willReturn(savedJudgment);

            // when
            judgmentCommandService.createJudgmentFromBoj(command);

            // then
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
            judgmentCommandService.deleteJudgment(command);

            // then
            verify(repository).deleteByIdAndUserId(100L, 1L);
        }
    }
}

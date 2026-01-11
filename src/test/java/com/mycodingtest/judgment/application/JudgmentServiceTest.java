package com.mycodingtest.judgment.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.judgment.application.dto.RegisterBojJudgmentCommand;
import com.mycodingtest.judgment.domain.Judgment;
import com.mycodingtest.judgment.domain.JudgmentRepository;
import com.mycodingtest.judgment.domain.JudgmentStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    @InjectMocks
    private JudgmentService judgmentService;

    @Mock
    private JudgmentRepository judgmentRepository;

    @Test
    void 문제ID와_유저ID로_판정_목록_조회() {
        // given
        Long problemId = 1L;
        Long userId = 1L;
        List<Judgment> judgments = List.of(Judgment.of(problemId, userId, 100L, JudgmentStatus.SUCCESS, Platform.BOJ, null, "code"));
        
        given(judgmentRepository.findByProblemIdAndUserId(problemId, userId)).willReturn(judgments);

        // when
        List<Judgment> result = judgmentService.readJudgments(problemId, userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getProblemId()).isEqualTo(problemId);
    }

    @Test
    void 제출_번호_존재_여부_확인() {
        // given
        Long submissionId = 12345L;
        Platform platform = Platform.BOJ;
        given(judgmentRepository.existsBySubmissionId(submissionId)).willReturn(true);

        // when
        boolean result = judgmentService.isJudgmentExist(submissionId, platform);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void 백준_판정_생성_성공() {
        // given
        Long problemId = 1L;
        Long userId = 2L;
        RegisterBojJudgmentCommand command = RegisterBojJudgmentCommand.builder()
                .baekjoonId("bojId")
                .submissionId(12345L)
                .resultText("Success")
                .memory(100)
                .time(200)
                .language("Java")
                .codeLength(300)
                .submittedAt(LocalDateTime.now())
                .sourceCode("code")
                .build();

        Judgment savedJudgment = Judgment.of(problemId, userId, command.getSubmissionId(), JudgmentStatus.SUCCESS, Platform.BOJ, null, command.getSourceCode());
        given(judgmentRepository.save(any(Judgment.class))).willReturn(savedJudgment);

        // when
        Judgment result = judgmentService.createJudgmentFromBoj(command, problemId, userId);

        // then
        assertThat(result.getSubmissionId()).isEqualTo(command.getSubmissionId());
        verify(judgmentRepository).save(any(Judgment.class));
    }

    @Test
    void 판정_삭제_성공() {
        // given
        Long judgmentId = 10L;
        Long userId = 2L;

        // when
        judgmentService.deleteJudgment(judgmentId, userId);

        // then
        verify(judgmentRepository).deleteByIdAndUserId(judgmentId, userId);
    }
}

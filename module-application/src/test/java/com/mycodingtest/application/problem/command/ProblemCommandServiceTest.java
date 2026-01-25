package com.mycodingtest.application.problem.command;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@MockitoSettings
class ProblemCommandServiceTest {

    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ProblemCommandService problemCommandService;

    @Nested
    class 문제_조회_또는_생성 {

        @Test
        void 문제가_이미_존재하면_기존_문제를_반환한다() {
            // given
            SyncProblemCommand command = new SyncProblemCommand(1000, "A+B", Platform.BOJ);
            Problem existingProblem = Problem.from(1000, "A+B", Platform.BOJ);
            given(problemRepository.findProblemByproblemNumberAndPlatform(1000, Platform.BOJ))
                    .willReturn(Optional.of(existingProblem));

            // when
            problemCommandService.syncProblem(command);

            // then
            verify(problemRepository, never()).save(any());
        }

        @Test
        void 문제가_없으면_새로_생성하여_저장한다() {
            // given
            SyncProblemCommand command = new SyncProblemCommand(1001, "A-B", Platform.BOJ);
            Problem newProblem = Problem.from(1L, 1001, "A-B", Platform.BOJ);
            given(problemRepository.findProblemByproblemNumberAndPlatform(1001, Platform.BOJ))
                    .willReturn(Optional.empty());
            given(problemRepository.saveAndFlush(any(Problem.class))).willReturn(newProblem);

            // when
            problemCommandService.syncProblem(command);

            // then

            ArgumentCaptor<Problem> captor = ArgumentCaptor.forClass(Problem.class);
            verify(problemRepository).saveAndFlush(captor.capture());

            Problem capturedProblem = captor.getValue();
            assertThat(capturedProblem.getProblemNumber()).isEqualTo(1001);
            assertThat(capturedProblem.getProblemTitle()).isEqualTo("A-B");
            assertThat(capturedProblem.getPlatform()).isEqualTo(Platform.BOJ);
        }
    }
}
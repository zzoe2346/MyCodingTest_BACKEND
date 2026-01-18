package com.mycodingtest.application.problem;

import com.mycodingtest.application.problem.dto.CreateProblemCommand;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {

    @Mock
    private ProblemRepository problemRepository;

    @InjectMocks
    private ProblemService problemService;

    @Nested
    class 문제_조회_또는_생성 {

        @Test
        void 문제가_이미_존재하면_기존_문제를_반환한다() {
            // given
            CreateProblemCommand command = new CreateProblemCommand(1000, "A+B", Platform.BOJ);
            Problem existingProblem = Problem.from( 1000, "A+B", Platform.BOJ);
            given(problemRepository.findProblemByproblemNumberAndPlatform(1000, Platform.BOJ))
                    .willReturn(Optional.of(existingProblem));

            // when
            Problem result = problemService.getOrCreateProblem(command);

            // then
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getProblemNumber()).isEqualTo(1000);
            verify(problemRepository, never()).save(any());
        }

        @Test
        void 문제가_없으면_새로_생성하여_저장한다() {
            // given
            CreateProblemCommand command = new CreateProblemCommand(1001, "A-B", Platform.BOJ);
            Problem newProblem = Problem.from(1001, "A-B", Platform.BOJ);
            given(problemRepository.findProblemByproblemNumberAndPlatform(1001, Platform.BOJ))
                    .willReturn(Optional.empty());
            given(problemRepository.save(any(Problem.class))).willReturn(newProblem);

            // when
            Problem result = problemService.getOrCreateProblem(command);

            // then
            assertThat(result.getId()).isEqualTo(2L);

            ArgumentCaptor<Problem> captor = ArgumentCaptor.forClass(Problem.class);
            verify(problemRepository).save(captor.capture());

            Problem capturedProblem = captor.getValue();
            assertThat(capturedProblem.getProblemNumber()).isEqualTo(1001);
            assertThat(capturedProblem.getProblemTitle()).isEqualTo("A-B");
            assertThat(capturedProblem.getPlatform()).isEqualTo(Platform.BOJ);
        }
    }
}

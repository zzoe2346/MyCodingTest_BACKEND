package com.mycodingtest.problem.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.problem.domain.ProblemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class ProblemServiceTest {

    @InjectMocks
    private ProblemService problemService;

    @Mock
    private ProblemRepository problemRepository;

    @Test
    void 이미_존재하는_백준_문제_조회_성공() {
        // given
        Integer problemNumber = 1000;
        String title = "A+B";
        Problem existingProblem = Problem.of(problemNumber, title, Platform.BOJ);
        
        given(problemRepository.findByProblemNumberAndPlatform(problemNumber, Platform.BOJ))
                .willReturn(Optional.of(existingProblem));

        // when
        Problem result = problemService.getOrCreateProblemFromBoj(problemNumber, title);

        // then
        assertThat(result).isEqualTo(existingProblem);
        verify(problemRepository, never()).save(any(Problem.class));
    }

    @Test
    void 존재하지_않는_백준_문제_생성_후_반환() {
        // given
        Integer problemNumber = 1001;
        String title = "A-B";
        Problem newProblem = Problem.of(problemNumber, title, Platform.BOJ);

        given(problemRepository.findByProblemNumberAndPlatform(problemNumber, Platform.BOJ))
                .willReturn(Optional.empty());
        given(problemRepository.save(any(Problem.class))).willReturn(newProblem);

        // when
        Problem result = problemService.getOrCreateProblemFromBoj(problemNumber, title);

        // then
        assertThat(result).isEqualTo(newProblem);
        verify(problemRepository).save(any(Problem.class));
    }
}

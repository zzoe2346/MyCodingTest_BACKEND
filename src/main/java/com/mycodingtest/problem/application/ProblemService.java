package com.mycodingtest.problem.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;

    public Problem getOrCreateProblemFromBoj(Integer problemNumber, String problemTitle) {
        return problemRepository.findByProblemNumberAndPlatform(problemNumber, Platform.BOJ)
                .orElseGet(() -> problemRepository.save(
                        Problem.of(problemNumber, problemTitle, Platform.BOJ)
                ));
    }

}

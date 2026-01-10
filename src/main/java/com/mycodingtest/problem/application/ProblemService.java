package com.mycodingtest.problem.application;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.query.dashboard.DashBoardQuearyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final DashBoardQuearyRepository dashBoardQuearyRepository;

    public Problem getOrCreateProblemFromBoj(Integer problemNumber, String problemTitle) {
        return dashBoardQuearyRepository.findByProblemNumberAndPlatform(problemNumber, Platform.BOJ)
                .orElseGet(() -> dashBoardQuearyRepository.save(
                        Problem.of(problemNumber, problemTitle, Platform.BOJ)
                ));
    }

}

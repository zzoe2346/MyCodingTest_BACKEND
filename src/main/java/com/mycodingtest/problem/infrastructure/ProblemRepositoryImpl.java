package com.mycodingtest.problem.infrastructure;

import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.problem.domain.ProblemRepository;
import com.mycodingtest.common.domain.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

    private final JpaProblemRepository jpaProblemRepository;

    @Override
    public Problem save(Problem problem) {
        return jpaProblemRepository.save(problem);
    }

    @Override
    public Optional<Problem> findByProblemNumberAndPlatform(Integer problemNumber, Platform platform) {
        return jpaProblemRepository.findByProblemNumberAndPlatform(problemNumber, platform);
    }
}

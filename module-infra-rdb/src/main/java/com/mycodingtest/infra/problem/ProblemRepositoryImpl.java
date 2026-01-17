package com.mycodingtest.infra.problem;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

    private final JpaProblemRepository repository;
    private final ProblemMapper mapper;

    @Override
    public Problem save(Problem problem) {
        ProblemEntity saved = repository.save(mapper.toEntity(problem));
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Problem> findProblem(Integer problemNumber, Platform platform) {
        return repository.findByProblemNumberAndPlatform(problemNumber, platform).map(mapper::toDomain);
    }
}

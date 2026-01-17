package com.mycodingtest.infra.problem;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.problem.Problem;
import com.mycodingtest.domain.problem.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProblemRepositoryImpl implements ProblemRepository {

    private final JpaProblemRepository repository;

    @Override
    public Problem save(Problem problem) {
        ProblemEntity saved = repository.save(ProblemEntity.from(problem));
        return saved.toDomain();
    }

    @Override
    public Optional<Problem> findProblemByproblemNumberAndPlatform(Integer problemNumber, Platform platform) {
        return repository.findByProblemNumberAndPlatform(problemNumber, platform).map(ProblemEntity::toDomain);
    }

    @Override
    public List<Problem> findAllByIdIn(List<Long> problemIds) {
        return repository.findAllById(problemIds).stream()
                .map(ProblemEntity::toDomain)
                .toList();
    }

}

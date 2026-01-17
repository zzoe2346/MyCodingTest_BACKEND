package com.mycodingtest.infra.problem;

import com.mycodingtest.domain.problem.Problem;
import org.springframework.stereotype.Component;

@Component
public class ProblemMapper {

    public Problem toDomain(ProblemEntity entity) {
        return Problem.builder()
                .id(entity.getId())
                .problemNumber(entity.getProblemNumber())
                .problemTitle(entity.getProblemTitle())
                .platform(entity.getPlatform())
                .build();
    }

    public ProblemEntity toEntity(Problem domain) {
        return ProblemEntity.builder()
                .problemNumber(domain.getProblemNumber())
                .problemTitle(domain.getProblemTitle())
                .platform(domain.getPlatform())
                .build();
    }
}

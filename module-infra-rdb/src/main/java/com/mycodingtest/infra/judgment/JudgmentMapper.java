package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.judgment.Judgment;
import org.springframework.stereotype.Component;

@Component
public class JudgmentMapper {

    public Judgment toDomain(JudgmentEntity entity) {
        return Judgment.builder()
                .id(entity.getId())
                .sourceCode(entity.getSourceCode())
                .status(entity.getStatus())
                .submissionId(entity.getSubmissionId())
                .userId(entity.getUserId())
                .platform(entity.getPlatform())
                .problemId(entity.getProblemId())
                .metaData(entity.getMetaData())
                .build();
    }

    public JudgmentEntity toEntity(Judgment domain) {
        return JudgmentEntity.builder()
                .sourceCode(domain.getSourceCode())
                .status(domain.getStatus())
                .submissionId(domain.getSubmissionId())
                .userId(domain.getUserId())
                .platform(domain.getPlatform())
                .problemId(domain.getProblemId())
                .metaData(domain.getMetaData())
                .build();
    }

}

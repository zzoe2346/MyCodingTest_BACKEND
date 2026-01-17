package com.mycodingtest.api.judgment.dto;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentStatus;
import com.mycodingtest.domain.judgment.MetaData;

public record JudgmentResponse(
        Long problemId,
        Long userId,
        Long submissionId,
        Long judgmentId,
        JudgmentStatus status,
        Platform platform,
        MetaData metaData,
        String sourceCode
) {
    public static JudgmentResponse from(Judgment entity) {
        return new JudgmentResponse(entity.getProblemId(), entity.getUserId(), entity.getSubmissionId(), entity.getId(), entity.getStatus(), entity.getPlatform(), entity.getMetaData(), entity.getSourceCode());
    }
}

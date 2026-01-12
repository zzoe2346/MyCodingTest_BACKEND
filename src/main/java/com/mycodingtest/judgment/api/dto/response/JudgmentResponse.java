package com.mycodingtest.judgment.api.dto.response;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.judgment.domain.Judgment;
import com.mycodingtest.judgment.domain.JudgmentStatus;
import com.mycodingtest.judgment.domain.MetaData;

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

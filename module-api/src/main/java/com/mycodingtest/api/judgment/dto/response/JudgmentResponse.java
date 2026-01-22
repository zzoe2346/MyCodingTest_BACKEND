package com.mycodingtest.api.judgment.dto.response;

import com.mycodingtest.application.judgment.query.JudgmentInfo;
import com.mycodingtest.domain.common.Platform;
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
    public static JudgmentResponse from(JudgmentInfo info) {
        return new JudgmentResponse(
                info.problemId(),
                info.userId(),
                info.submissionInfo().getSubmissionId(),
                info.id(),
                info.submissionInfo().getStatus(),
                info.submissionInfo().getPlatform(),
                info.submissionInfo().getMetaData(),
                info.submissionInfo().getSourceCode()
        );
    }
}

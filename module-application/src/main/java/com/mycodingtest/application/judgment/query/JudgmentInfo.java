package com.mycodingtest.application.judgment.query;

import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.SubmissionInfo;

public record JudgmentInfo(
        Long id,
        Long problemId,
        Long userId,
        SubmissionInfo submissionInfo
) {
    public static JudgmentInfo from(Judgment judgment) {
        return new JudgmentInfo(
                judgment.getId(),
                judgment.getProblemId(),
                judgment.getUserId(),
                judgment.getSubmissionInfo()
        );
    }
}

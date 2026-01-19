package com.mycodingtest.domain.judgment;

import com.mycodingtest.domain.common.Platform;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubmissionInfo {
    private final Long submissionId;
    private final Platform platform;
    private final JudgmentStatus status;
    private final MetaData metaData;
    private final String sourceCode;

    public static SubmissionInfo from(Long submissionId, Platform platform, JudgmentStatus status, MetaData metaData, String sourceCode) {
        if (submissionId == null) throw new IllegalArgumentException("제출 ID는 필수입니다");
        if (platform == null) throw new IllegalArgumentException("플랫폼은 필수입니다");
        if (status == null) throw new IllegalArgumentException("채점 상태는 필수입니다");
        return SubmissionInfo.builder()
                .submissionId(submissionId)
                .platform(platform)
                .status(status)
                .metaData(metaData)
                .sourceCode(sourceCode)
                .build();
    }
}

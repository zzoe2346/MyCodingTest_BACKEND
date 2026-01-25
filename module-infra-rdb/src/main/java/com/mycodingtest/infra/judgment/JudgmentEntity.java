package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.SubmissionInfo;
import com.mycodingtest.domain.judgment.JudgmentStatus;
import com.mycodingtest.domain.judgment.MetaData;
import com.mycodingtest.infra.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
@Table(name = "judgment")
public class JudgmentEntity extends BaseEntity {

    private Long problemId;
    private Long userId;

    private Long submissionId;
    @Enumerated(EnumType.STRING)
    private JudgmentStatus status;
    @Enumerated(EnumType.STRING)
    private Platform platform;
    @Convert(converter = MetaDataConverter.class)
    @Column(columnDefinition = "longtext")
    private MetaData metaData;
    @Column(columnDefinition = "longtext")
    private String sourceCode;

    public Judgment toDomain() {
        return Judgment.builder()
                .id(getId())
                .problemId(this.problemId)
                .userId(userId)
                .submissionInfo(SubmissionInfo.from(submissionId, platform, status, metaData, sourceCode))
                .build();
    }

    public static JudgmentEntity from(Judgment domain) {
        SubmissionInfo info = domain.getSubmissionInfo();
        return JudgmentEntity.builder()
                .problemId(domain.getProblemId())
                .userId(domain.getUserId())
                .submissionId(info.getSubmissionId())
                .status(info.getStatus())
                .platform(info.getPlatform())
                .metaData(info.getMetaData())
                .sourceCode(info.getSourceCode())
                .build();
    }

}
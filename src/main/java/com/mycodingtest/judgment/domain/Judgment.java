package com.mycodingtest.judgment.domain;

import com.mycodingtest.common.domain.Platform;
import com.mycodingtest.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Judgment extends BaseEntity {

    private Long problemId;
    private Long userId;
    private Long submissionId; // 플랫폼에서 스크래핑한 제출 결과의 유니크 값. 이 값으로 Judgment의 중복방지

    @Enumerated(EnumType.STRING)
    private JudgmentStatus status;

    @Enumerated(EnumType.STRING)
    private Platform platform;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    private MetaData metaData;

    public static Judgment of(
            Long problemId,
            Long userId,
            Long submissionId,
            JudgmentStatus status,
            Platform platform,
            MetaData metaData
    ) {
        return Judgment.builder()
                .problemId(problemId)
                .userId(userId)
                .submissionId(submissionId)
                .status(status)
                .platform(platform)
                .metaData(metaData)
                .build();
    }
}

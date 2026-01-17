package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.common.Platform;
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
    private String sourceCode;

}
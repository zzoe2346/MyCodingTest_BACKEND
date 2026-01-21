package com.mycodingtest.application.judgment.support;

import com.mycodingtest.application.judgment.command.CreateBojJudgmentCommand;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.SubmissionInfo;
import org.springframework.stereotype.Component;

@Component
public class BojJudgmentAssembler {

    public Judgment assemble(CreateBojJudgmentCommand command) {
        BojMetaData metaData = BojMetaData.builder()
                .baekjoonId(command.baekjoonId())
                .codeLength(command.codeLength())
                .language(command.language())
                .resultText(command.resultText())
                .memory(command.memory())
                .time(command.time())
                .submissionId(command.submissionId())
                .submittedAt(command.submittedAt())
                .build();

        return Judgment.from(
                command.problemId(),
                command.userId(),
                SubmissionInfo.from(
                        command.submissionId(),
                        Platform.BOJ,
                        Platform.BOJ.toStatus(command.resultText()),
                        metaData,
                        command.sourceCode()
                )
        );
    }

}

package com.mycodingtest.application.judgment;

import com.mycodingtest.application.judgment.dto.CreateBojJudgmentCommand;
import com.mycodingtest.application.judgment.dto.DeleteJudgmentCommand;
import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.BojMetaData;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <h3>채점 기록 관리 서비스 (JudgmentService)</h3>
 * <p>
 * 플랫폼별 채점 상세 내역을 저장하고 조회하는 유스케이스를 담당합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class JudgmentService {

    private final JudgmentRepository judgmentRepository;

    /**
     * 특정 문제와 사용자에 대한 모든 채점 기록을 최신순으로 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<Judgment> readJudgments(Long problemId, Long userId) {
        return judgmentRepository.findByProblemIdAndUserId(problemId, userId);
    }

    /**
     * 외부 플랫폼의 특정 제출 기록이 이미 시스템에 등록되어 있는지 확인합니다.
     */
    @Transactional(readOnly = true)
    public boolean isJudgmentExist(Long submissionId, Platform platform) {
        return judgmentRepository.existsBySubmissionId(submissionId, platform);
    }

    /**
     * <b>백준 전용 채점 결과 생성</b>
     * <p>
     * 전달받은 커맨드 객체로부터 백준 특화 메타데이터를 추출하여 {@link JudgmentEntity} 엔티티를 생성하고 저장합니다.
     * </p>
     */
    @Transactional
    public Judgment createJudgmentFromBoj(CreateBojJudgmentCommand command) {
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

        Judgment judgment = Judgment.from(
                command.problemId(),
                command.userId(),
                command.submissionId(),
                Platform.BOJ.toStatus(command.resultText()),
                Platform.BOJ,
                metaData,
                command.sourceCode());
        return judgmentRepository.save(judgment);
    }

    /**
     * 특정 채점 기록을 삭제합니다.
     */
    @Transactional
    public void deleteJudgment(DeleteJudgmentCommand command) {
        judgmentRepository.deleteByIdAndUserId(command.judgmentId(), command.userId());
    }
}
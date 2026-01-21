package com.mycodingtest.application.judgment.command;

import com.mycodingtest.application.judgment.support.BojJudgmentAssembler;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * TODO javadoc 수정
 * <h3>채점 기록 관리 서비스 (JudgmentService)</h3>
 * <p>
 * 플랫폼별 채점 상세 내역을 저장하고 조회하는 유스케이스를 담당합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class JudgmentCommandService {

    private final JudgmentRepository judgmentRepository;
    private final BojJudgmentAssembler bojAssembler;

    /**
     * <b>백준 전용 채점 결과 생성</b>
     * <p>
     * 전달받은 커맨드 객체로부터 백준 특화 메타데이터를 추출하여 엔티티를 생성하고 저장합니다.
     * </p>
     */
    @Transactional
    public void createJudgmentFromBoj(CreateBojJudgmentCommand command) {
        Judgment judgment = bojAssembler.assemble(command);
        judgmentRepository.save(judgment);
    }

    /**
     * 특정 채점 기록을 삭제합니다.
     */
    @Transactional
    public void deleteJudgment(DeleteJudgmentCommand command) {
        judgmentRepository.deleteByIdAndUserId(command.judgmentId(), command.userId());
    }
}
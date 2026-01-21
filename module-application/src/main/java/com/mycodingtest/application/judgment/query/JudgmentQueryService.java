package com.mycodingtest.application.judgment.query;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * TODO: Javadoc 수정
 * <h3>채점 기록 관리 서비스 (JudgmentService)</h3>
 * <p>
 * 플랫폼별 채점 상세 내역을 저장하고 조회하는 유스케이스를 담당합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class JudgmentQueryService {

    private final JudgmentRepository judgmentRepository;

    /**
     * 특정 문제와 사용자에 대한 모든 채점 기록을 최신순으로 조회합니다.
     */
    @Transactional(readOnly = true)
    public List<JudgmentInfo> getJudgments(Long problemId, Long userId) {
        return judgmentRepository.findByProblemIdAndUserId(problemId, userId)
                .stream()
                .map(JudgmentInfo::from)
                .toList();
    }

    /**
     * 외부 플랫폼의 특정 제출 기록이 이미 시스템에 등록되어 있는지 확인합니다.
     */
    @Transactional(readOnly = true)
    public boolean isJudgmentExist(Long submissionId, Platform platform) {
        return judgmentRepository.existsBySubmissionId(submissionId, platform);
    }
}
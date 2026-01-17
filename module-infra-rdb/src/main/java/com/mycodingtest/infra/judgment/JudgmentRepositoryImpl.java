package com.mycodingtest.infra.judgment;

import com.mycodingtest.domain.common.Platform;
import com.mycodingtest.domain.judgment.Judgment;
import com.mycodingtest.domain.judgment.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class JudgmentRepositoryImpl implements JudgmentRepository {

    private final JpaJudgmentRepository jpaJudgmentRepository;

    @Override
    public Judgment save(Judgment judgment) {
        JudgmentEntity save = jpaJudgmentRepository.save(JudgmentEntity.from(judgment));
        return save.toDomain();
    }

    @Override
    public boolean existsBySubmissionId(Long submissionId, Platform platform) {
        return jpaJudgmentRepository.existsBySubmissionIdAndPlatform(submissionId, platform);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpaJudgmentRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public List<Judgment> findByProblemIdAndUserId(Long problemId, Long userId) {
        return jpaJudgmentRepository.findByProblemIdAndUserId(problemId, userId)
                .stream()
                .map(JudgmentEntity::toDomain)
                .toList();
    }

}

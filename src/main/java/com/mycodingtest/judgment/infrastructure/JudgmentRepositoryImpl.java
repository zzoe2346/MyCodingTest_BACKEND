package com.mycodingtest.judgment.infrastructure;

import com.mycodingtest.judgment.domain.Judgment;
import com.mycodingtest.judgment.domain.JudgmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JudgmentRepositoryImpl implements JudgmentRepository {

    private final JpaJudgmentRepository jpaJudgmentRepository;

    @Override
    public Judgment save(Judgment judgment) {
        return jpaJudgmentRepository.save(judgment);
    }

    @Override
    public Optional<Judgment> findById(Long id) {
        return jpaJudgmentRepository.findById(id);
    }

    @Override
    public boolean existsBySubmissionId(Long submissionId) {
        return jpaJudgmentRepository.existsBySubmissionId(submissionId);
    }

    @Override
    public void deleteByIdAndUserId(Long id, Long userId) {
        jpaJudgmentRepository.deleteByIdAndUserId(id, userId);
    }

    @Override
    public List<Judgment> findByProblemIdAndUserId(Long problemId, Long userId) {
        return jpaJudgmentRepository.findByProblemIdAndUserId(problemId, userId);
    }
}

package com.mycodingtest.solvedproblemtag;

import com.mycodingtest.common.exception.ResourceNotFoundException;
import com.mycodingtest.problem.domain.Problem;
import com.mycodingtest.problem.domain.ProblemRepository;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagResponse;
import com.mycodingtest.solvedproblemtag.dto.AlgorithmTagSetRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SolvedProblemTagService {

    private final ProblemRepository solvedProblemRepository;
    private final SolvedProblemTagRepository solvedProblemTagRepository;

    public SolvedProblemTagService(ProblemRepository solvedProblemRepository, SolvedProblemTagRepository solvedProblemTagRepository) {
        this.solvedProblemRepository = solvedProblemRepository;
        this.solvedProblemTagRepository = solvedProblemTagRepository;
    }

    @Transactional
    public void setAlgorithmTags(Long solvedProblemId, AlgorithmTagSetRequest request, Long userId) {
        Problem problem = solvedProblemRepository.findById(solvedProblemId)
                .orElseThrow(ResourceNotFoundException::new);
        problem.validateOwnership(userId);
        if (request.tagIds().length == 0) {
            solvedProblemTagRepository.deleteAllBySolvedProblem(problem);
            return;
        }

        List<SolvedProblemTag> existingSolvedProblemTags = solvedProblemTagRepository.findAllBySolvedProblem(problem);

        List<SolvedProblemTag> tagsToRemove = existingSolvedProblemTags.stream()
                .filter(tag -> Arrays.stream(request.tagIds()).noneMatch(reqTagId -> reqTagId == tag.getTagId()))
                .toList();

        Set<SolvedProblemTag> newSolvedProblemTags = new HashSet<>();
        for (int tagId : request.tagIds()) {
            newSolvedProblemTags.add(new SolvedProblemTag(problem, tagId));
        }

        solvedProblemTagRepository.deleteAllInBatch(tagsToRemove);
        solvedProblemTagRepository.saveAll(newSolvedProblemTags);
    }

    @Transactional(readOnly = true)
    public AlgorithmTagResponse getAlgorithmTags(Long solvedProblemId) {
        List<Integer> tagIds = solvedProblemRepository.findTagIdsBySolvedProblemId(solvedProblemId);
        return new AlgorithmTagResponse(tagIds.toArray());
    }
}

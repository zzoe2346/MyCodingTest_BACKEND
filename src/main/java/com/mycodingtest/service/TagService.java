package com.mycodingtest.service;

import com.mycodingtest.dto.AlgorithmTagResponse;
import com.mycodingtest.dto.AlgorithmTagSetRequest;
import com.mycodingtest.entity.SolvedProblem;
import com.mycodingtest.entity.SolvedProblemTag;
import com.mycodingtest.exception.InvalidOwnershipException;
import com.mycodingtest.exception.ResourceNotFoundException;
import com.mycodingtest.repository.SolvedProblemRepository;
import com.mycodingtest.repository.SolvedProblemTagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {

    private final SolvedProblemRepository solvedProblemRepository;
    private final SolvedProblemTagRepository solvedProblemTagRepository;

    public TagService(SolvedProblemRepository solvedProblemRepository, SolvedProblemTagRepository solvedProblemTagRepository) {
        this.solvedProblemRepository = solvedProblemRepository;
        this.solvedProblemTagRepository = solvedProblemTagRepository;
    }

    @Transactional
    public void setAlgorithmTags(Long solvedProblemId, AlgorithmTagSetRequest request, Long userId) {
        SolvedProblem solvedProblem = solvedProblemRepository.findById(solvedProblemId)
                .orElseThrow(ResourceNotFoundException::new);
        if (!solvedProblem.getUser().getId().equals(userId)) throw new InvalidOwnershipException();

        if (request.tagIds().length == 0) {
            solvedProblemTagRepository.deleteAllBySolvedProblem(solvedProblem);
            return;
        }

        List<SolvedProblemTag> existingSolvedProblemTags = solvedProblemTagRepository.findAllBySolvedProblem(solvedProblem);

        List<SolvedProblemTag> tagsToRemove = existingSolvedProblemTags.stream()
                .filter(tag -> Arrays.stream(request.tagIds()).noneMatch(reqTagId -> reqTagId == tag.getTagId()))
                .toList();

        Set<SolvedProblemTag> newSolvedProblemTags = new HashSet<>();
        for (int tagId : request.tagIds()) {
            newSolvedProblemTags.add(new SolvedProblemTag(solvedProblem, tagId));
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

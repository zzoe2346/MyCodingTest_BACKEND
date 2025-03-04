package com.mycodingtest.solvedproblemtag;

import com.mycodingtest.solvedproblem.SolvedProblem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolvedProblemTagRepository extends JpaRepository<SolvedProblemTag, SolvedProblemTag.SolvedProblemTagId> {
    void deleteAllBySolvedProblem(SolvedProblem solvedProblem);

    List<SolvedProblemTag> findAllBySolvedProblem(SolvedProblem solvedProblem);
}

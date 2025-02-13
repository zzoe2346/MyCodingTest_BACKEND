package com.mycodingtest.entity;

import java.io.Serializable;
import java.util.Objects;

public class SolvedProblemTagId implements Serializable {
    private int tagId;
    private Long solvedProblem;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolvedProblemTagId that = (SolvedProblemTagId) o;
        return tagId == that.tagId && Objects.equals(solvedProblem, that.solvedProblem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagId, solvedProblem);
    }
}

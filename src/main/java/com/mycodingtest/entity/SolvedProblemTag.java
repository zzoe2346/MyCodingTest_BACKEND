package com.mycodingtest.entity;

import jakarta.persistence.*;

@Entity
@IdClass(SolvedProblemTagId.class)
public class SolvedProblemTag {
    @Id
    private int tagId;
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private SolvedProblem solvedProblem;

    public SolvedProblemTag(SolvedProblem solvedProblem, int tagId) {
        this.solvedProblem = solvedProblem;
        this.tagId = tagId;
    }

    public SolvedProblemTag() {
    }

    public int getTagId() {
        return tagId;
    }
}

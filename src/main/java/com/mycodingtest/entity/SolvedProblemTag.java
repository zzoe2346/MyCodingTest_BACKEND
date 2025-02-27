package com.mycodingtest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
@IdClass(SolvedProblemTag.SolvedProblemTagId.class)
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

    public static class SolvedProblemTagId implements Serializable {
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
}

package com.mycodingtest.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
public class SolvedProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int problemNumber;

    private String problemTitle;

    private LocalDateTime recentSubmitAt;

    private String recentResultText;

    private boolean favorited = false;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Review review;

    @OneToMany(mappedBy = "solvedProblem", cascade = CascadeType.REMOVE)
    Set<SolvedProblemTag> tags = new HashSet<>();

    public SolvedProblem(int problemNumber, String problemTitle, User user, Review review) {
        this.problemNumber = problemNumber;
        this.problemTitle = problemTitle;
        this.user = user;
        this.review = review;
    }

    public void updateRecentResult(LocalDateTime submittedAt, String resultText) {
        this.recentSubmitAt = submittedAt;
        this.recentResultText = resultText;
    }

    public void reverseFavoriteStatus() {
        favorited = !favorited;
    }
}

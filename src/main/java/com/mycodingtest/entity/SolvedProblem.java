package com.mycodingtest.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
public class SolvedProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int problemNumber;
    private String problemTitle;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private Review review;
    private LocalDateTime recentSubmitAt;
    private String recentResultText;
    private boolean favorited = false;
    @OneToMany(mappedBy = "solvedProblem", cascade = CascadeType.REMOVE)
    Set<SolvedProblemTag> tags = new HashSet<>();

    public SolvedProblem(int problemNumber, String problemTitle, User user, Review review) {
        this.problemNumber = problemNumber;
        this.problemTitle = problemTitle;
        this.user = user;
        this.review = review;
    }

    protected SolvedProblem() {
    }

    public Review getReview() {
        return review;
    }

    public void setRecentResultText(String recentResultText) {
        this.recentResultText = recentResultText;
    }

    public void setRecentSubmitAt(LocalDateTime recentSubmitAt) {
        this.recentSubmitAt = recentSubmitAt;
    }

    public Long getId() {
        return id;
    }

    public int getProblemNumber() {
        return problemNumber;
    }

    public void changeFavorite() {
        favorited = !favorited;
    }

    public Set<SolvedProblemTag> getTags() {
        return tags;
    }

    public User getUser() {
        return user;
    }
}

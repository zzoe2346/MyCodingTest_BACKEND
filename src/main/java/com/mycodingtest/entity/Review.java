package com.mycodingtest.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int difficultyLevel = -1;
    private int importanceLevel = -1;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
    private boolean reviewed;
    private LocalDateTime reviewedAt;

    public Review(User user) {
        this.user = user;
    }

    protected Review() {
    }

    public Long getId() {
        return id;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getImportanceLevel() {
        return importanceLevel;
    }

    public void setImportanceLevel(int importanceLevel) {
        this.importanceLevel = importanceLevel;
    }

    public boolean isReviewed() {
        return reviewed;
    }

    public void setReviewed(boolean reviewed) {
        this.reviewed = reviewed;
    }

    public LocalDateTime getReviewedAt() {
        return reviewedAt;
    }

    public void setReviewedAt(LocalDateTime reviewedAt) {
        this.reviewedAt = reviewedAt;
    }

    public User getUser() {
        return user;
    }
}

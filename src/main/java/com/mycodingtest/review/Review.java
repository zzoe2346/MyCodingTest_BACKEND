package com.mycodingtest.review;

import com.mycodingtest.user.User;
import com.mycodingtest.common.exception.InvalidOwnershipException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int difficultyLevel = -1;

    private int importanceLevel = -1;

    private boolean reviewed;

    private LocalDateTime reviewedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Review(User user) {
        this.user = user;
    }

    public void updateRatingLevels(int difficultyLevel, int importanceLevel) {
        this.difficultyLevel = difficultyLevel;
        this.importanceLevel = importanceLevel;
    }

    public void validateOwnership(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new InvalidOwnershipException("User " + userId + " does not own this review");
        }
    }

    public void completeReview() {
        reviewed = true;
        reviewedAt = LocalDateTime.now();
    }
}

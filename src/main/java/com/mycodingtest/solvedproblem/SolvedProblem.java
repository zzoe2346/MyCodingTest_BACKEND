package com.mycodingtest.solvedproblem;

import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.review.Review;
import com.mycodingtest.solvedproblemtag.SolvedProblemTag;
import com.mycodingtest.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
public class SolvedProblem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    private int problemNumber;

    @Getter
    private String problemTitle;

    @Getter
    private LocalDateTime recentSubmitAt;

    @Getter
    private String recentResultText;

    @Getter
    private boolean favorited = false;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Getter
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

    public void validateOwnership(Long userId) {
        if (!this.user.getId().equals(userId)) {
            throw new InvalidOwnershipException();
        }
    }
}

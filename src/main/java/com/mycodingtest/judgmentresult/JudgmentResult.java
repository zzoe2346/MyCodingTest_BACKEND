package com.mycodingtest.judgmentresult;

import com.mycodingtest.problem.SolvedProblem;
import com.mycodingtest.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class JudgmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private Long submissionId;

    @Getter
    private String baekjoonId;

    @Getter
    private int problemId;

    @Getter
    private String resultText;

    @Getter
    private int memory;

    @Getter
    private int time;

    @Getter
    private String language;

    @Getter
    private int codeLength;

    @Getter
    private LocalDateTime submittedAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private SolvedProblem solvedProblem;

    public JudgmentResult(String baekjoonId, int codeLength, String language, int memory, int problemId, String resultText, Long submissionId, LocalDateTime submittedAt, int time, User user, SolvedProblem solvedProblem) {
        this.baekjoonId = baekjoonId;
        this.codeLength = codeLength;
        this.language = language;
        this.memory = memory;
        this.problemId = problemId;
        this.resultText = resultText;
        this.submissionId = submissionId;
        this.submittedAt = submittedAt;
        this.time = time;
        this.user = user;
        this.solvedProblem = solvedProblem;
    }
}

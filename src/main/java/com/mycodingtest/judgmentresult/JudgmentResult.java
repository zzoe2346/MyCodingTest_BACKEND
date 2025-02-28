package com.mycodingtest.judgmentresult;

import com.mycodingtest.user.User;
import com.mycodingtest.solvedproblem.SolvedProblem;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class JudgmentResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long submissionId;

    private String baekjoonId;

    private int problemId;

    private String resultText;

    private int memory;

    private int time;

    private String language;

    private int codeLength;

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

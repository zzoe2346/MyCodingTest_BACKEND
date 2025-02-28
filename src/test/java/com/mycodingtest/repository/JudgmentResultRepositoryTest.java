package com.mycodingtest.repository;

import com.mycodingtest.judgmentresult.JudgmentResult;
import com.mycodingtest.judgmentresult.JudgmentResultRepository;
import com.mycodingtest.review.Review;
import com.mycodingtest.solvedproblem.SolvedProblem;
import com.mycodingtest.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class JudgmentResultRepositoryTest {

    @Autowired
    JudgmentResultRepository judgmentResultRepository;
    @Autowired
    private TestEntityManager entityManager;

    private SolvedProblem solvedProblem1;
    private JudgmentResult result1;
    private JudgmentResult result2;
    private User user;
    @BeforeEach
    void setTestData() {
        user = new User();
        user = entityManager.persist(user);

        solvedProblem1 = new SolvedProblem(1000, "Problem 1", user, new Review(user));
        entityManager.persist(solvedProblem1);

        SolvedProblem solvedProblem2 = new SolvedProblem(1001, "Problem 2", user, new Review(user));
        entityManager.persist(solvedProblem2);

        result1 = new JudgmentResult("user1", 100, "Java", 1024, 1000, "Accepted", 1L, LocalDateTime.now(), 100, user, solvedProblem1);
        entityManager.persist(result1);

        result2 = new JudgmentResult("user1", 150, "Python", 2048, 1000, "Wrong Answer", 2L, LocalDateTime.now().minusDays(1), 150, user, solvedProblem1);
        entityManager.persist(result2);

        JudgmentResult result3 = new JudgmentResult("user2", 200, "C++", 4096, 1001, "Accepted", 3L, LocalDateTime.now().minusDays(2), 200, user, solvedProblem2);
        entityManager.persist(result3);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void findBySolvedProblemIdOrderBySubmissionIdDesc() {
        //when
        List<JudgmentResult> resultList = judgmentResultRepository.findJudgmentResultsWithUserBySolvedProblemIdAndUserIdOrderBySubmissionIdDesc(solvedProblem1.getId(),user.getId());

        //then
        assertThat(resultList).hasSize(2);
        assertThat(resultList.get(0).getSubmissionId()).isEqualTo(result2.getSubmissionId());
        assertThat(resultList.get(1).getSubmissionId()).isEqualTo(result1.getSubmissionId());
    }

    @Test
    void existsBySubmissionId() {
        //when
        boolean result1 = judgmentResultRepository.existsBySubmissionId(1L);
        boolean result2 = judgmentResultRepository.existsBySubmissionId(3L);
        boolean result3 = judgmentResultRepository.existsBySubmissionId(5L);

        //then
        assertThat(result1).isTrue();
        assertThat(result2).isTrue();
        assertThat(result3).isFalse();
    }
}
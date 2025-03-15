-- H2 데이터베이스를 위한 더미 데이터 삽입 SQL

-- User 테이블 더미 데이터
INSERT INTO "user" (created_at, email, name, oauth_id, oauth_provider, picture) VALUES
                                                                                    (CURRENT_TIMESTAMP, 'user1@example.com', 'User One', 'oauth123', 'google', 'http://example.com/user1.jpg'),
                                                                                    (CURRENT_TIMESTAMP, 'user2@example.com', 'User Two', 'oauth456', 'github', 'http://example.com/user2.jpg'),
                                                                                    (CURRENT_TIMESTAMP, 'user3@example.com', 'User Three', 'oauth789', 'kakao', 'http://example.com/user3.jpg');

-- Review 테이블 더미 데이터 (User와 1:1 관계 고려)
INSERT INTO review (difficulty_level, importance_level, reviewed, reviewed_at, "user_id") VALUES
                                                                                              (3, 4, TRUE, CURRENT_TIMESTAMP, 1),
                                                                                              (4, 5, TRUE, CURRENT_TIMESTAMP, 2),
                                                                                              (2, 1, False, null, 3);

-- Solved_Problem 테이블 더미 데이터 (User, Review와 관계 고려)
INSERT INTO solved_problem (favorited, problem_number, recent_submit_at, review_id, "user_id", problem_title, recent_result_text) VALUES
                                                                                                                                      (TRUE, 1000, CURRENT_TIMESTAMP, 1, 1, 'A+B', 'AC'),
                                                                                                                                      (FALSE, 1001, CURRENT_TIMESTAMP, 2, 2, 'Hello World', 'AC'),
                                                                                                                                      (TRUE, 1002, CURRENT_TIMESTAMP, 3, 3, '??!', 'AC');

-- Judgment_Result 테이블 더미 데이터 (Solved_Problem, User와 관계 고려)
-- 여러 judgment result를 가질 수 있도록 구성.
INSERT INTO judgment_result (code_length, memory, problem_id, time, solved_problem_id, submission_id, submitted_at, "user_id", baekjoon_id, language, result_text) VALUES
                                                                                                                                                                       (100, 2048, 1000, 100, 1, 12345, CURRENT_TIMESTAMP, 1, 'user1_baekjoon', 'Java', 'AC'),
                                                                                                                                                                       (150, 3072, 1000, 120, 1, 12346, CURRENT_TIMESTAMP, 1, 'user1_baekjoon', 'Python', 'WA'),
                                                                                                                                                                       (120, 2560, 1001, 80, 2, 67890, CURRENT_TIMESTAMP, 2, 'user2_baekjoon', 'C++', 'AC'),
                                                                                                                                                                       (120, 2560, 1002, 80, 3, 67891, CURRENT_TIMESTAMP, 3, 'user3_baekjoon', 'C++', 'AC');

-- Solved_Problem_Tag 테이블 더미 데이터 (Solved_Problem과 관계, tag_id는 임의의 값)
INSERT INTO solved_problem_tag (tag_id, solved_problem_id) VALUES
                                                               (1, 1), -- Problem 1000, tag: DP
                                                               (2, 1), -- Problem 1000, tag: Implementation
                                                               (3, 2),  -- problem 1001, tag: string
                                                               (4, 3); -- problem 1002, tag: math
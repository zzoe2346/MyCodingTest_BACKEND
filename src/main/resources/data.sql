-- User Data
INSERT INTO "user" (id, email, name, picture, oauth_provider, oauth_id, created_at)
VALUES (1, 'test@example.com', 'TestUser', 'https://via.placeholder.com/150', 'google', '123456789', CURRENT_TIMESTAMP());

INSERT INTO "user" (id, email, name, picture, oauth_provider, oauth_id, created_at)
VALUES (2, 'dev@example.com', 'DevUser', 'https://via.placeholder.com/150', 'github', '987654321', CURRENT_TIMESTAMP());

-- Problem Data
INSERT INTO problem (id, problem_number, problem_title, platform, created_at, updated_at)
VALUES (1, 1000, 'A+B', 'BOJ', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO problem (id, problem_number, problem_title, platform, created_at, updated_at)
VALUES (2, 1001, 'A-B', 'BOJ', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO problem (id, problem_number, problem_title, platform, created_at, updated_at)
VALUES (3, 2557, 'Hello World', 'BOJ', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

INSERT INTO problem (id, problem_number, problem_title, platform, created_at, updated_at)
VALUES (4, 42, 'Programmers Problem', 'Programmers', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Judgment Data
INSERT INTO judgment (id, problem_id, user_id, submission_id, status, platform, meta_data, created_at, updated_at, source_code)
VALUES (1, 1, 1, 10001, 'SUCCESS', 'BOJ', '{"type": "BOJ", "submissionId": 10001, "baekjoonId": "testuser", "resultText": "맞았습니다!!", "memory": 14200, "time": 120, "language": "Java 11", "codeLength": 500, "submittedAt": "2024-01-01T12:00:00"}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'sourceCode');

INSERT INTO judgment (id, problem_id, user_id, submission_id, status, platform, meta_data, created_at, updated_at, source_code)
VALUES (2, 2, 1, 10002, 'FAIL', 'BOJ', '{"type": "BOJ", "submissionId": 10002, "baekjoonId": "testuser", "resultText": "틀렸습니다", "memory": 14200, "time": 120, "language": "Java 11", "codeLength": 500, "submittedAt": "2024-01-02T12:00:00"}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'sourceCode');

INSERT INTO judgment (id, problem_id, user_id, submission_id, status, platform, meta_data, created_at, updated_at, source_code)
VALUES (3, 1, 2, 10003, 'SUCCESS', 'BOJ', '{"type": "BOJ", "submissionId": 10003, "baekjoonId": "devuser", "resultText": "맞았습니다!!", "memory": 14000, "time": 100, "language": "Python 3", "codeLength": 200, "submittedAt": "2024-01-01T13:00:00"}', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP(), 'sourceCode');

-- Review Data
-- Review for Problem 1 by User 1 (Completed)
INSERT INTO review (id, problem_id, user_id, content, reviewed, difficulty_level, importance_level, source_code, reviewed_at, status, favorited, recent_submit_at, recent_result, created_at, updated_at)
VALUES (1, 1, 1, 'Very easy basic problem.', true, 1, 1, 'import java.util.*; ...', CURRENT_TIMESTAMP(), 'COMPLETED', false, CURRENT_TIMESTAMP(), 'SUCCESS', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Review for Problem 2 by User 1 (To Do)
INSERT INTO review (id, problem_id, user_id, content, reviewed, difficulty_level, importance_level, source_code, reviewed_at, status, favorited, recent_submit_at, recent_result, created_at, updated_at)
VALUES (2, 2, 1, '', false, -1, -1, NULL, NULL, 'TO_DO', true, CURRENT_TIMESTAMP(), 'FAIL', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Review for Problem 3 by User 2 (In Progress)
INSERT INTO review (id, problem_id, user_id, content, reviewed, difficulty_level, importance_level, source_code, reviewed_at, status, favorited, recent_submit_at, recent_result, created_at, updated_at)
VALUES (3, 3, 2, 'Need to review output format.', false, 1, 2, 'print("Hello World")', NULL, 'IN_PROGRESS', false, CURRENT_TIMESTAMP(), 'SUCCESS', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
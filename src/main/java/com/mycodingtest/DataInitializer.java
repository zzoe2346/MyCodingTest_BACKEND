package com.mycodingtest;

import com.mycodingtest.dto.JudgmentResultSaveRequest;
import com.mycodingtest.entity.User;
import com.mycodingtest.repository.UserRepository;
import com.mycodingtest.service.JudgmentResultService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final JudgmentResultService judgmentResultService;

    public DataInitializer(UserRepository userRepository, JudgmentResultService judgmentResultService) {
        this.userRepository = userRepository;
        this.judgmentResultService = judgmentResultService;
    }

    private User mockUser;

    @Override
    public void run(String... args) {
        //for test
        //setMockUser();
        //setMockJudgmentResultPost();
    }

    private void setMockJudgmentResultPost() {
        for (int i = 1; i <= 16; i++) {
            JudgmentResultSaveRequest request = new JudgmentResultSaveRequest((long) i, "zzoe2346", i, "title" + i, "fail", 100, 100, "Java 11", 100, LocalDateTime.now().minusDays(i));
            judgmentResultService.saveJudgmentResult(request, mockUser.getId());
        }
    }

    private void setMockUser() {
        mockUser = userRepository.save(new User("정성훈", "tjdgns5506@gmail.com", "http://k.kakaocdn.net/dn/qLJJx/btsJUFp5zQR/BK0drGiBSHZpwKYUJapXz1/img_110x110.jpg", "kakao", "3881812464"));
    }
}

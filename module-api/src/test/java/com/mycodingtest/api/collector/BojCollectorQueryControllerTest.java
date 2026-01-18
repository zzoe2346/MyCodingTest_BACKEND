package com.mycodingtest.api.collector;

import com.mycodingtest.application.collector.BojIngestionService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;

@ExtendWith(MockitoExtension.class)
class BojCollectorQueryControllerTest {

    @Mock
    private BojIngestionService bojIngestionService;

    @InjectMocks
    private BojCollectorQueryController controller;

    @Nested
    class 중복_제출_확인 {

        @Test
        void 중복이_아니면_OK를_반환한다() {
            // given
            Long submissionId = 12345L;
            willDoNothing().given(bojIngestionService).checkDuplicatedSubmissionId(submissionId);

            // when
            ResponseEntity<Void> result = controller.getDuplicatedSubmissionIdCheckResult(submissionId);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        }

        @Test
        void 중복이면_CONFLICT를_반환한다() {
            // given
            Long submissionId = 99999L;
            willThrow(new IllegalStateException("이미 존재하는 제출 번호입니다"))
                    .given(bojIngestionService).checkDuplicatedSubmissionId(submissionId);

            // when
            ResponseEntity<Void> result = controller.getDuplicatedSubmissionIdCheckResult(submissionId);

            // then
            assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        }
    }
}

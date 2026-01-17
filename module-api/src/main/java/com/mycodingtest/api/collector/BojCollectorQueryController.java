package com.mycodingtest.api.collector;

import com.mycodingtest.application.collector.BojIngestionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RestController
@RequiredArgsConstructor
public class BojCollectorQueryController {

    private final BojIngestionService bojIngestionService;

    @GetMapping("/api/boj/check/{submissionId}")
    @Operation(summary = "이미 제출된 채점 결과인지 확인", description = "특정 채점 결과의 제출 코드를 조회합니다.")
    public ResponseEntity<Void> getDuplicatedSubmissionIdCheckResult(@PathVariable Long submissionId) {
        try {
            bojIngestionService.checkDuplicatedSubmissionId(submissionId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
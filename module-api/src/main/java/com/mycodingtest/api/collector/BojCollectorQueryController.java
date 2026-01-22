package com.mycodingtest.api.collector;

import com.mycodingtest.application.collector.command.BojIngestionCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수집", description = "문제, 채점 등 정보 수집 관련 API")
@RestController
@RequiredArgsConstructor
public class BojCollectorQueryController {

    private final BojIngestionCommandService bojIngestionCommandService;

    @GetMapping("/api/boj/check/{submissionId}")
    @Operation(summary = "이미 제출된 채점 결과인지 확인", description = "특정 채점 결과의 제출 코드를 조회합니다.")
    public ResponseEntity<Void> getDuplicatedSubmissionIdCheckResult(@PathVariable Long submissionId) {
        try {
            bojIngestionCommandService.checkDuplicatedSubmissionId(submissionId);
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

}
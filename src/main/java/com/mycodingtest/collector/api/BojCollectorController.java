package com.mycodingtest.collector.api;

import com.mycodingtest.collector.application.BojIngestionService;
import com.mycodingtest.collector.api.dto.CreateBojProblemAndJudgmentRequest;
import com.mycodingtest.common.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RestController
@RequiredArgsConstructor
public class BojCollectorController {

    private final BojIngestionService bojIngestionService;

    @PostMapping("/api/boj")
    @Operation(summary = "채점 결과 저장", description = "크롬 익스텐션으로 부터 전송된 채점 결과를 저장합니다.")
    public ResponseEntity<Void> createProblemAndJudgment(@RequestBody CreateBojProblemAndJudgmentRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        bojIngestionService.ingest(request.toCommand(), userDetails.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

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
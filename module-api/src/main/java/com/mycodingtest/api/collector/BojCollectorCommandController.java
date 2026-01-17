package com.mycodingtest.api.collector;

import com.mycodingtest.application.collector.BojIngestionService;
import com.mycodingtest.api.collector.dto.CreateProblemAndJudgmentRequest;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "수집", description = "문제, 채점 등 정보 수집 관련 API")
@RestController
@RequiredArgsConstructor
public class BojCollectorCommandController {

    private final BojIngestionService bojIngestionService;

    @PostMapping("/api/boj")
    @Operation(summary = "채점 결과 저장", description = "크롬 익스텐션으로 부터 전송된 채점 결과를 저장합니다.")
    public ResponseEntity<Void> createProblemAndJudgment(@RequestBody CreateProblemAndJudgmentRequest request,
                                                         @AuthenticationPrincipal CustomUserDetails userDetails) {
        bojIngestionService.ingest(request.toCommand(userDetails.getUserId()));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
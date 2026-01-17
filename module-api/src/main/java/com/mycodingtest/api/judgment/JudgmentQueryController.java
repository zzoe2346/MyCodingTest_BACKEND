package com.mycodingtest.api.judgment;

import com.mycodingtest.application.judgment.JudgmentService;
import com.mycodingtest.api.judgment.dto.response.JudgmentResponse;
import com.mycodingtest.application.judgment.dto.ReadJudgmentsCommand;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "채점 결과", description = "채점 결과 관련 API")
@RequiredArgsConstructor
public class JudgmentQueryController {

    private final JudgmentService judgmentService;

    @GetMapping("/api/judgments")
    @Operation(summary = "채점 결과 목록 조회", description = "특정 문제의 채점 결과 목록을 조회합니다.")
    public ResponseEntity<List<JudgmentResponse>> getJudgmentResultList(
            @RequestParam Long problemId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity
                .ok(judgmentService.readJudgments(ReadJudgmentsCommand.from(problemId, userDetails.getUserId()))
                        .stream()
                        .map(JudgmentResponse::from)
                        .toList());
    }

}

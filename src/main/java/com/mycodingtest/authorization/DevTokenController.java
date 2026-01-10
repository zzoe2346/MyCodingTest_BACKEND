package com.mycodingtest.authorization;

import com.mycodingtest.common.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "Dev Only", description = "개발 및 테스트용 API")
@Profile("local")
public class DevTokenController {

    private final JwtUtil jwtUtil;

    @GetMapping("/api/dev/token")
    @Operation(summary = "테스트용 JWT 토큰 발급", description = "특정 User ID에 대한 JWT 토큰을 발급합니다. (로컬 환경 전용)")
    public ResponseEntity<String> getTestToken(
            @RequestParam(defaultValue = "1") Long userId,
            @RequestParam(defaultValue = "TestUser") String name,
            @RequestParam(defaultValue = "https://via.placeholder.com/150") String picture
    ) {
        String token = jwtUtil.generateToken(userId, picture, name);
        return ResponseEntity.ok(token);
    }

}

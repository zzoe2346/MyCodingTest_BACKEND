package com.mycodingtest.authorization;

import com.mycodingtest.authorization.dto.UserInfoResponse;
import com.mycodingtest.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "인증", description = "유저의 인증 상태에 관한 API")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/api/sign-out")
    @Operation(summary = "요청시, 로그아웃이 된다. 발급된 쿠키가 만료되는 로직이 작동함.")
    public ResponseEntity<Void> signOut(HttpServletResponse response) {
        authService.signOut(response);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/me")
    @Operation(summary = "요청시, 로그인를 상태 체크한다. 200 응답: 로그인 상태  401 응답: 비로그인 상태이다. DB Hit 없이 쿠키의 유효성을 확인함.")
    public ResponseEntity<UserInfoResponse> checkMe(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(authService.getUserInfo(userDetails.getPicture(), userDetails.getUsername()));
    }
}

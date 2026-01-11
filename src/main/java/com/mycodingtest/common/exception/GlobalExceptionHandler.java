package com.mycodingtest.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;

/**
 * <h3>글로벌 예외 핸들러 (GlobalExceptionHandler)</h3>
 * <p>
 * 애플리케이션 전역에서 발생하는 예외를 감지하여 사용자에게 일관된 에러 응답(RFC 7807 - Problem Details)을 반환합니다.
 * </p>
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 리소스를 찾을 수 없는 경우 (404 Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e) {
        return createProblemDetail(HttpStatus.NOT_FOUND, "Resource not found", e.getMessage());
    }

    /**
     * 데이터 중복 등 잘못된 요청 상태인 경우 (409 Conflict 또는 400 Bad Request)
     */
    @ExceptionHandler(IllegalStateException.class)
    public ProblemDetail handleIllegalStateException(IllegalStateException e) {
        return createProblemDetail(HttpStatus.CONFLICT, "Conflict", e.getMessage());
    }

    /**
     * 소유권이 없는 리소스에 접근하려는 경우 (403 Forbidden)
     */
    @ExceptionHandler(InvalidOwnershipException.class)
    public ProblemDetail handleInvalidOwnershipException(InvalidOwnershipException e) {
        return createProblemDetail(HttpStatus.FORBIDDEN, "Access Denied", "본인의 리소스만 접근 가능합니다.");
    }

    /**
     * 처리되지 않은 모든 예외 (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneralException(Exception e) {
        log.error("Unhandled Exception: ", e);
        return createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", e.getMessage());
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
        problemDetail.setTitle(title);
        return problemDetail;
    }
}
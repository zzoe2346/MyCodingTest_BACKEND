package com.mycodingtest.common;

import com.mycodingtest.common.exception.InvalidOwnershipException;
import com.mycodingtest.common.exception.NotOurUserException;
import com.mycodingtest.common.exception.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@MockitoSettings
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler exceptionHandler;

    @Test
    @DisplayName("MethodArgumentNotValidException 처리 시 400 상태코드를 반환한다")
    void handleValidationExceptions() {
        // given
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);

        // when
        var response = exceptionHandler.handleValidationExceptions(ex);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    @DisplayName("NotOurUserException 처리 시 403 상태코드를 반환한다")
    void handleNotOurUserException() {
        // given
        NotOurUserException ex = new NotOurUserException();

        // when
        ErrorResponse response = exceptionHandler.handleNotOurUserException(ex);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    @DisplayName("ResourceNotFoundException 처리 시 404 상태코드를 반환한다")
    void handleResourceNotFoundException() {
        // given
        ResourceNotFoundException ex = new ResourceNotFoundException();

        // when
        ErrorResponse response = exceptionHandler.handleResourceNotFoundException(ex);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("InvalidOwnershipException 처리 시 403 상태코드를 반환한다")
    void handleInvalidOwnershipException() {
        // given
        InvalidOwnershipException ex = new InvalidOwnershipException();

        // when
        ErrorResponse response = exceptionHandler.handleInvalidOwnershipException(ex);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Nested
    class UncaughtException {
        @Test
        @DisplayName("Exception 발생 시 500 상태코드를 반환한다")
        void handleAllUncaughtException_when_Exception() {
            // given
            Exception ex = new Exception("예상치 못한 오류가 발생했습니다");

            // when
            ErrorResponse response = exceptionHandler.handleAllUncaughtException(ex);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("EntityNotFoundException 예외 발생 시 500 상태코드를 반환한다")
        void handleAllUncaughtException_when_EntityNotFoundException() {
            // given
            EntityNotFoundException ex = new EntityNotFoundException();

            // when
            ErrorResponse response = exceptionHandler.handleAllUncaughtException(ex);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @Test
        @DisplayName("IllegalArgumentException 발생 시 500 상태코드를 반환한다")
        void handleAllUncaughtException_when_() {
            // given
            IllegalArgumentException ex = new IllegalArgumentException();

            // when
            ErrorResponse response = exceptionHandler.handleAllUncaughtException(ex);

            // then
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

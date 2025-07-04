package com.sesac7.hellopet.global.exception;

import com.sesac7.hellopet.domain.application.validation.AlreadyProcessedApplicationException;
import com.sesac7.hellopet.domain.application.validation.DuplicateApplicationException;
import com.sesac7.hellopet.global.exception.custom.UnauthorizedException;
import com.sesac7.hellopet.global.exception.custom.WithdrawUserException;
import com.sesac7.hellopet.global.exception.dto.response.ExceptionResponse;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ResponseStatusException -> 구체적인 예외 클래스로 변경
    // 모든 팀원 수정 이후 메서드 삭제 예정
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> responseStatusExceptionHandler(ResponseStatusException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name());

        return ResponseEntity.status(e.getStatusCode()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException e) {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach((error) -> {
            String fieldName = error.getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(WithdrawUserException.class)
    public ResponseEntity<ExceptionResponse> responseInactivationUserExceptionHandler(WithdrawUserException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ExceptionResponse> responseUnauthorizedUserExceptionHandler(UnauthorizedException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .header(HttpHeaders.SET_COOKIE, e.getAccessCookie().toString())
                             .header(HttpHeaders.SET_COOKIE, e.getRefreshCookie().toString())
                             .body(response);
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ExceptionResponse> responseDuplicateApplicationExceptionHandler(
            DuplicateApplicationException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(AlreadyProcessedApplicationException.class)
    public ResponseEntity<ExceptionResponse> responseAlreadyProcessedApplicationExceptionHandler(
            AlreadyProcessedApplicationException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> responseEntityNotFoundExceptionHandler(
            EntityNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> responseAccessDeniedExceptionHandler(
            AccessDeniedException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> responseNoSuchElementExceptionHandler(
            NoSuchElementException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.NOT_FOUND.value(),
                HttpStatus.NOT_FOUND.name());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    // 다른 구체적인 핸들러에서 처리되지 않은 예외가 발생했을 때 마지막으로 호출
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> responseExceptionHandler(
            Exception e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.name());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> responseIllegalArgumentExceptionHandler(
            IllegalArgumentException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> responseUsernameNotFoundExceptionHandler(
            UsernameNotFoundException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> responseAuthorizationDeniedExceptionHandler(
            AuthorizationDeniedException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.name());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> responseBadCredentialsExceptionHandler(
            BadCredentialsException e) {
        ExceptionResponse response = ExceptionResponse.of(e,
                HttpStatus.UNAUTHORIZED.value(),
                HttpStatus.UNAUTHORIZED.name());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}

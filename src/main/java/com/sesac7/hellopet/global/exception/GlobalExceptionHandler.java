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

    // 다른 구체적인 핸들러에서 처리되지 않은 예외가 발생했을 때 마지막으로 호출
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> responseExceptionHandler(Exception e) {
        return generateExceptionResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // ResponseStatusException -> 구체적인 예외 클래스로 변경
    // 모든 팀원 수정 이후 메서드 삭제 예정
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ExceptionResponse> responseStatusExceptionHandler(ResponseStatusException e) {
        return generateExceptionResponse(e, HttpStatus.BAD_REQUEST);
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

    @ExceptionHandler(WithdrawUserException.class)
    public ResponseEntity<ExceptionResponse> responseInactivationUserExceptionHandler(WithdrawUserException e) {
        return generateExceptionResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(DuplicateApplicationException.class)
    public ResponseEntity<ExceptionResponse> responseDuplicateApplicationExceptionHandler(
            DuplicateApplicationException e) {
        return generateExceptionResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AlreadyProcessedApplicationException.class)
    public ResponseEntity<ExceptionResponse> responseAlreadyProcessedApplicationExceptionHandler(
            AlreadyProcessedApplicationException e) {
        return generateExceptionResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> responseEntityNotFoundExceptionHandler(EntityNotFoundException e) {
        return generateExceptionResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> responseAccessDeniedExceptionHandler(AccessDeniedException e) {
        return generateExceptionResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ExceptionResponse> responseNoSuchElementExceptionHandler(NoSuchElementException e) {
        return generateExceptionResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionResponse> responseIllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return generateExceptionResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> responseUsernameNotFoundExceptionHandler(UsernameNotFoundException e) {
        return generateExceptionResponse(e, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ExceptionResponse> responseAuthorizationDeniedExceptionHandler(
            AuthorizationDeniedException e) {
        return generateExceptionResponse(e, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> responseBadCredentialsExceptionHandler(BadCredentialsException e) {
        return generateExceptionResponse(e, HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<ExceptionResponse> generateExceptionResponse(Exception e, HttpStatus status) {
        ExceptionResponse response = ExceptionResponse.of(e, status.value(), status.name());
        return ResponseEntity.status(status).body(response);
    }
}

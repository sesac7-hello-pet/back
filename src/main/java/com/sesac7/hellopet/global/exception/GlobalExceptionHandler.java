package com.sesac7.hellopet.global.exception;

import com.sesac7.hellopet.domain.application.validation.AlreadyProcessedApplicationException;
import com.sesac7.hellopet.domain.application.validation.DuplicateApplicationException;
import com.sesac7.hellopet.global.exception.custom.UnauthorizedException;
import com.sesac7.hellopet.global.exception.custom.WithdrawUserException;
import com.sesac7.hellopet.global.exception.dto.response.ExceptionResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
}

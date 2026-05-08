package com.nitin.payment.common;

import com.nitin.payment.common.exception.DuplicateResourceException;
import com.nitin.payment.common.exception.FileProcessingException;
import com.nitin.payment.common.exception.InvalidRequestException;
import com.nitin.payment.common.exception.KafkaProcessingException;
import com.nitin.payment.common.exception.ResourceNotFoundException;
import com.nitin.payment.common.exception.UnauthorizedException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiResponse<Void>> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(404, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(DuplicateResourceException.class)
    ResponseEntity<ApiResponse<Void>> conflict(DuplicateResourceException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(409, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(UnauthorizedException.class)
    ResponseEntity<ApiResponse<Void>> unauthorized(UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.error(401, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(FileProcessingException.class)
    ResponseEntity<ApiResponse<Void>> fileProcessing(FileProcessingException ex) {
        return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, ex.getMessage(), List.of(rootMessage(ex))));
    }

    @ExceptionHandler(KafkaProcessingException.class)
    ResponseEntity<ApiResponse<Void>> kafka(KafkaProcessingException ex) {
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .body(ApiResponse.error(502, ex.getMessage(), List.of(rootMessage(ex))));
    }

    @ExceptionHandler({InvalidRequestException.class, ConstraintViolationException.class})
    ResponseEntity<ApiResponse<Void>> badRequest(RuntimeException ex) {
        return ResponseEntity.badRequest().body(ApiResponse.error(400, ex.getMessage(), List.of(ex.getMessage())));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse<Void>> validation(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(ApiResponse.error(400, "Validation failed", errors));
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiResponse<Void>> generic(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.error(500, "Unexpected server error", List.of(ex.getMessage())));
    }

    private String rootMessage(Throwable throwable) {
        Throwable cursor = throwable;
        while (cursor.getCause() != null) {
            cursor = cursor.getCause();
        }
        return cursor.getMessage() == null ? throwable.getMessage() : cursor.getMessage();
    }
}

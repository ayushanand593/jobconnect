package com.DcoDe.jobconnect.Exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestControllerAdvice
public class GlobalExceptionHandler  {

//
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//
//        ApiError apiError = new ApiError(
//                HttpStatus.BAD_REQUEST,
//                "Validation error",
//                errors
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> onAnyError(Exception ex) {
        ex.printStackTrace();                            // ← add this
//        log.error("Unhandled exception:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "status","INTERNAL_SERVER_ERROR",
                        "message", ex.getMessage(),
                        "errors", null
                ));
    }
//
//    @ExceptionHandler(ConfigDataResourceNotFoundException.class)
//    public ResponseEntity<Object> handleResourceNotFoundException(ConfigDataResourceNotFoundException ex) {
//        ApiError apiError = new ApiError(
//                HttpStatus.NOT_FOUND,
//                ex.getMessage(),
//                null
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(AccessDeniedException.class)
//    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
//        ApiError apiError = new ApiError(
//                HttpStatus.FORBIDDEN,
//                "Access denied",
//                null
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
//        ApiError apiError = new ApiError(
//                HttpStatus.NOT_FOUND,
//                ex.getMessage(),
//                null
//        );
//        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleAllExceptions(Exception ex) {
//        ApiError apiError = new ApiError(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "An unexpected error occurred",
//                null
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @Data
//    @AllArgsConstructor
//    public static class ApiError {
//        private HttpStatus status;
//        private String message;
//        private Map<String, String> errors;
//    }
}

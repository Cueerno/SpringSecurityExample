package com.radiuk.securityexample.handler;

import com.radiuk.securityexample.exception.UserNotCreatedException;
import com.radiuk.securityexample.exception.UserNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Forbidden");
        errorResponse.put("pisss", "pisss");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", HttpStatus.FORBIDDEN.value());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<?> handleUserNotCreatedException(UserNotCreatedException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not created");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not found");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Incorrect username or password");
        errorResponse.put("message", exception.getMessage());
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> defaultMessage = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Incorrect fields");
        errorResponse.put("message", defaultMessage);
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
}

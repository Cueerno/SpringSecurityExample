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

import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponseStructure(
                        OffsetDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "Lack of access",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(UserNotCreatedException.class)
    public ResponseEntity<?> handleUserNotCreatedException(UserNotCreatedException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseStructure(
                        OffsetDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "User not created",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ExceptionResponseStructure(
                        OffsetDateTime.now(),
                        HttpStatus.NOT_FOUND.value(),
                        "User not found",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException exception) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ExceptionResponseStructure(
                        OffsetDateTime.now(),
                        HttpStatus.UNAUTHORIZED.value(),
                        "Incorrect username or password",
                        exception.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<String> defaultMessage = exception.getBindingResult().getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ExceptionResponseStructure(
                        OffsetDateTime.now(),
                        HttpStatus.BAD_REQUEST.value(),
                        "Incorrect data",
                        defaultMessage
                ));
    }
}

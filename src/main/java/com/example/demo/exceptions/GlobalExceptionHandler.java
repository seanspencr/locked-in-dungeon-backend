package com.example.demo.exceptions;

import com.example.demo.dto.ExceptionResponse;
import com.example.demo.exceptions.user.UserAlreadyExistException;
import com.example.demo.exceptions.user.UserNotFoundException;
import com.example.demo.exceptions.user.UserWrongCredentialException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(exception = UserNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFound(RuntimeException e, HttpServletRequest request){
        ExceptionResponse error = new ExceptionResponse(
                Instant.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(exception = UserWrongCredentialException.class)
    public ResponseEntity<ExceptionResponse> handleWrongCredential(RuntimeException e, HttpServletRequest request){
        ExceptionResponse error = new ExceptionResponse(
                Instant.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Wrong username or password",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }



    @ExceptionHandler(exception = UserAlreadyExistException.class)
    public ResponseEntity<ExceptionResponse> handleUserAlreadyExist(UserAlreadyExistException e, HttpServletRequest request){
        ExceptionResponse error = new ExceptionResponse(
                Instant.now(),
                HttpStatus.CONFLICT.value(),
                "User already exist",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(exception = Exception.class)
    public ResponseEntity<ExceptionResponse> exceptionFallback(Exception e, HttpServletRequest request){
        ExceptionResponse error = new ExceptionResponse(
                Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal server error",
                e.getMessage(),
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}

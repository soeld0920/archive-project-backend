package com.archive.archive_project_backend.controller.advice;

import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.login.LoginException;
import com.archive.archive_project_backend.exception.login.SignupException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class LoginExceptionHandler {

    @ExceptionHandler(LoginException.class)
    public ResponseEntity<String> BadRequestExceptionHandler(LoginException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

package com.archive.archive_project_backend.controller.advice;

import com.archive.archive_project_backend.exception.FindUserException;
import com.archive.archive_project_backend.exception.login.LoginException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

    @ExceptionHandler(FindUserException.class)
    public ResponseEntity<String> BadRequestExceptionHandler(FindUserException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

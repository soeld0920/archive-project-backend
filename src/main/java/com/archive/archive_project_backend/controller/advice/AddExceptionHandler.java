package com.archive.archive_project_backend.controller.advice;

import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.add.AddException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AddExceptionHandler {

    @ExceptionHandler(AddException.class)
    public ResponseEntity<String> AddExceptionHandler(AddException e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

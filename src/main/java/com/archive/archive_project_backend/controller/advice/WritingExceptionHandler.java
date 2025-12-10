package com.archive.archive_project_backend.controller.advice;

import com.archive.archive_project_backend.exception.AddWritingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class WritingExceptionHandler {

    @ExceptionHandler(AddWritingException.class)
    public ResponseEntity<String> handleAddWritingFail(AddWritingException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}

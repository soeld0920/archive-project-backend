package com.archive.archive_project_backend.controller.advice;

import com.archive.archive_project_backend.exception.file.FileException;
import com.archive.archive_project_backend.exception.login.LoginException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FileExceptionHandler {

    @ExceptionHandler(FileException.class)
    public ResponseEntity<String> FileExceptionHandler(FileException e){
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}

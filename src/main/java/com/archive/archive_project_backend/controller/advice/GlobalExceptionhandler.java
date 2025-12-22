package com.archive.archive_project_backend.controller.advice;

import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionhandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> BadRequestExceptionHandler(BadRequestException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String>NotFoundExceptionHandler(NotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<Map<String, String>>> validationHandler(MethodArgumentNotValidException e){
        List<Map<String, String>> errorRes = null;

        BindingResult bindingResult = e.getBindingResult();

        if(bindingResult.hasErrors()){
            errorRes = bindingResult.getFieldErrors()
                    .stream()
                    .map(fieldError -> Map.of(
                            fieldError.getField(), fieldError.getDefaultMessage()
                    )).collect(Collectors.toList());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorRes);
    }
}

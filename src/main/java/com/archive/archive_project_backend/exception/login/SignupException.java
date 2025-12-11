package com.archive.archive_project_backend.exception.login;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SignupException extends RuntimeException{
    private HttpStatus httpStatus;

    public SignupException(String message, HttpStatus httpStatus){
        super(message);
        this.httpStatus = httpStatus;
    }
}

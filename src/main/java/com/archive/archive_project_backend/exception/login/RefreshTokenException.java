package com.archive.archive_project_backend.exception.login;

import org.springframework.http.HttpStatus;

public class RefreshTokenException extends LoginException {
    public RefreshTokenException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}

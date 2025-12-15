package com.archive.archive_project_backend.exception.login;

import org.springframework.http.HttpStatus;

public class NotLoginUserException extends LoginException {
    public NotLoginUserException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}

package com.archive.archive_project_backend.exception.login;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class SigninException extends LoginException {
    public SigninException(String message, HttpStatus httpStatus){
        super(message, httpStatus);
    }
}

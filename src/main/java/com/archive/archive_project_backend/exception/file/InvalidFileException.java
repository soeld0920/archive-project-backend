package com.archive.archive_project_backend.exception.file;

import org.springframework.http.HttpStatus;

public class InvalidFileException extends FileException {
    public InvalidFileException() {
        super("올바르지 않은 파일 형식입니다.", HttpStatus.BAD_REQUEST);
    }
}

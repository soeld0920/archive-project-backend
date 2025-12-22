package com.archive.archive_project_backend.exception.file;

import org.springframework.http.HttpStatus;

public class EmptyFileException extends FileException {
    public EmptyFileException() {
        super("파일이 비어있습니다.", HttpStatus.BAD_REQUEST);
    }
}

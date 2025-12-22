package com.archive.archive_project_backend.exception.file;

import org.springframework.http.HttpStatus;

public class PathTraversalException extends FileException {
    public PathTraversalException() {
        super("잘못된 경로입니다.", HttpStatus.BAD_REQUEST);
    }
}

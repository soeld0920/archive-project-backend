package com.archive.archive_project_backend.exception.add;

public class AddCommentException extends AddException {
    public AddCommentException() {
        super("댓글 추가 과정에서 에러가 발생했습니다.");
    }
}

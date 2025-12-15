package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {

    int insertComment(Comment comment);

    List<Comment> selectCommentsByWritingUuid(String writingUuid);

    //댓글의 다음 번호를 얻기 위한 유틸
    int getNextCommentOrder(String writingUuid);
}

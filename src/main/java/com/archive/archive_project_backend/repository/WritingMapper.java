package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Writing;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WritingMapper {

    //글 추가
    int insertWriting(Writing writing);

    //uuid로 글 찾기
    Writing selectWritingByUuid(String uuid);
}

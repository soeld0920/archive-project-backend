package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Writing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface WritingMapper {

    //글 추가
    int insertWriting(Writing writing);

    //uuid로 글 찾기
    Writing selectWritingByUuid(String uuid);

    //좋아요 여부 및 북마크 여부 반환
    boolean getGreatedByUuids(
            @Param("writingUuid") String writingUuid,
            @Param("userUuid") String userUuid
            );
    boolean getBookmarkedByUuids(
            @Param("writingUuid") String writingUuid,
            @Param("userUuid") String userUuid
    );
}

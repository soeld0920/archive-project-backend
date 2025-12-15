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

    //좋아요 토글
    int insertGreated(
            @Param("writingUuid") String writingUuid,
            @Param("userUuid") String userUuid
    );

    int deleteGreated(
            @Param("writingUuid") String writingUuid,
            @Param("userUuid") String userUuid
    );

    //북마크 토글
    int insertBookmarked(
            @Param("writingUuid") String writingUuid,
            @Param("userUuid") String userUuid
    );

    int deleteBookmarked(
            @Param("writingUuid") String writingUuid,
            @Param("userUuid") String userUuid
    );

    //메타데이터 증가
    int increaseView(
            @Param("writingUuid") String writingUuid
    );
    int decreaseView(
            @Param("writingUuid") String writingUuid
    );
    int increaseGreat(
            @Param("writingUuid") String writingUuid
    );
    int decreaseGreat(
            @Param("writingUuid") String writingUuid
    );

    //여부만 반환
    boolean existsWritingByUuid(String writingUuid);
}

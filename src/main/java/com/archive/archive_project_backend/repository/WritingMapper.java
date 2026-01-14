package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Writing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Mapper
public interface WritingMapper {

    //글 추가
    int insertWriting(
            @Param("writing") Writing writing
    );

    //uuid로 글 찾기
    Writing selectWritingByUuid(String uuid);

    //authorUuid 찾기
    String getAuthorUuidByUuid(String uuid);

    //시리즈 글들 반환
    List<Writing> selectWritingBySeriesUuid(String uuid);

    //List<String> -> List<Writing>
    List<Writing> selectWritingsByStrings(List<String> writingUuidList);

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

    //글 삭제
    int deleteWritingByUuid(String uuid);

    //모든 좋아요 제거
    int deleteAllGreat(String uuid);

    //모든 북마크 제거
    int deleteAllBookmarked(String uuid);

    //todo : 댓글 하나 제거

    //댓글 전부 제거
    int deleteAllComment(String uuid);

    //변경
    int updateCategory(@Param("writingUuid") String writingUuid, @Param("categoryId") int categoryId);
    int updateContent(@Param("writingUuid") String writingUuid, @Param("content") String content);
    int updateTitle(@Param("writingUuid") String writingUuid, @Param("title") String title);
    int updateSeries(@Param("writingUuid") String writingUuid, @Param("seriesUuid") String seriesUuid, @Param("seriesOrder") Integer seriesOrder);
    int updateTime(@Param("writingUuid") String writingUuid);
    //series_order 변경
    int reorderSeriesOrder(@Param("seriesUuid") String seriesUuid, @Param("startOrder") int startOrder);

    //다중 series_order 업데이트
    int updateSeriesOrderByList(
            @Param("writingList") List<Writing> writingList
    );
}

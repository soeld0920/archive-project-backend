package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.dto.req.FindWritingReqDto;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.exception.AddWritingException;
import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.FindWritingException;
import com.archive.archive_project_backend.repository.CategoryMapper;
import com.archive.archive_project_backend.repository.SeriesMapper;
import com.archive.archive_project_backend.repository.UserMapper;
import com.archive.archive_project_backend.repository.WritingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WritingService {
    private final WritingMapper writingMapper;
    private final CategoryMapper categoryMapper;
    private final SeriesMapper seriesMapper;
    private final UserMapper userMapper;

    //단건 글 추가
    @Transactional(rollbackFor = Exception.class)
    public void addWriting(AddWritingReqDto dto, String authorUuid){

        //식별자 생성 및 entity 받아옴
        String uuid = UUID.randomUUID().toString();
        User author = userMapper.getUserByUuid(authorUuid);
        Category category = categoryMapper.getCategoryById(dto.getCategoryId());
        Series series = seriesMapper.getSeriesByUuid(dto.getSeriesUuid());

        //entity 생성
        Writing writing = dto.toEntity(uuid,author, category, series);

        //추가
        int successCount = writingMapper.insertWriting(writing);
        if(successCount < 1){
            throw new AddWritingException("글 추가 과정 중 에러가 발생했습니다.");
        }
    }

    //단건 글 조회
    //조회 시 로그인한 유저도 받아봐서 좋아요 여부, 북마크 여부도 반환해야함.
    @Transactional(rollbackFor = Exception.class)
    public FindWritingResDto findWritingByUuid(FindWritingReqDto reqDto){
        //가드
        if(reqDto.getWritingUuid() == null || reqDto.getUserUuid() == null){
            throw new BadRequestException("글 찾기의 요청이 이상합니다.");
        }

        //글 받아오기
        Writing writing = writingMapper.selectWritingByUuid(reqDto.getWritingUuid());

        if(writing == null){
            throw new FindWritingException("해당 uuid의 글이 없습니다.");
        }

        //좋아요 및 북마크 여부
        boolean greated = writingMapper.getGreatedByUuids(reqDto.getWritingUuid(), reqDto.getUserUuid());
        boolean bookmarked = writingMapper.getBookmarkedByUuids(reqDto.getWritingUuid(), reqDto.getUserUuid());

        return FindWritingResDto.builder().writing(writing).greated(greated).bookmarked(bookmarked).build();

    }

}

package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.dto.req.FindWritingReqDto;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.entity.*;
import com.archive.archive_project_backend.exception.add.AddTagException;
import com.archive.archive_project_backend.exception.add.AddWritingException;
import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.FindWritingException;
import com.archive.archive_project_backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class WritingService {
    private final WritingMapper writingMapper;
    private final CategoryMapper categoryMapper;
    private final SeriesMapper seriesMapper;
    private final UserMapper userMapper;
    private final TagMapper tagMapper;

    //글과 tag를 연결
    @Transactional(rollbackFor = Exception.class)
    private void addTags(List<Tag> tags){

    }

    //단건 글 추가
    @Transactional(rollbackFor = Exception.class)
    public void addWriting(AddWritingReqDto dto, String authorUuid){

        //식별자 생성 및 entity 받아옴
        String uuid = UUID.randomUUID().toString();
        User author = userMapper.getUserByUuid(authorUuid);

        Category category = categoryMapper.getCategoryById(dto.getCategoryId());
        //category 없음 -> error 반환
        if(category == null) {
            throw new BadRequestException("존재하지 않는 카테고리입니다.");
        }

        Series series = null;
        if(dto.getSeriesUuid() != null){
            series = seriesMapper.getSeriesByUuid(dto.getSeriesUuid());
            if(series == null){
                throw new BadRequestException("존재하지 않는 시리즈입니다.");
            }
        }

        //태그 중복 제거
        dto.setTag(new ArrayList<>(new LinkedHashSet<>(dto.getTag())));

        //entity 생성
        Writing writing = dto.toEntity(uuid,author, category, series);

        //추가
        int successCount = writingMapper.insertWriting(writing);
        if(successCount < 1){
            throw new AddWritingException();
        }

        //태그 추가
        tagMapper.insertTags(writing.getTag());

        //id 얻어오기
        List<Tag> tags = tagMapper.getTagsByTagNames(writing.getTag());

        //이를 연결함
        successCount = tagMapper.insertWritingTags(writing.getWritingUuid(), tags);

        if(successCount < tags.size()){
            throw new AddTagException();
        }
    }

    //단건 글 조회
    //조회 시 댓글을 제외한 정보들을 반환해줘야함
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

        //태그 추가
        List<Tag> tags = tagMapper.selectTagsByWritingUuid(writing.getWritingUuid());
        writing.setTag(tags);

        return FindWritingResDto.builder().writing(writing).build();
    }

}

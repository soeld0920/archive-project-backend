package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.exception.AddWritingException;
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
    public void addWriting(AddWritingReqDto dto){

        //식별자 생성 및 entity 받아옴
        String uuid = UUID.randomUUID().toString();
        User author = userMapper.getUserByUuid(dto.getAuthorUuid());
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

}

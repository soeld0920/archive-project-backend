package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddSeriesReqDto;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.exception.FindUserException;
import com.archive.archive_project_backend.exception.add.AddSeriesException;
import com.archive.archive_project_backend.repository.SeriesMapper;
import com.archive.archive_project_backend.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesMapper seriesMapper;
    private final UserMapper userMapper;

    public void addSeries(AddSeriesReqDto dto, String authorUuid){
        String seriesUuid = UUID.randomUUID().toString();
        Series series = dto.toEntity(seriesUuid, authorUuid);

        int successCount = seriesMapper.insertSeries(series);
        if(successCount < 1){
            throw new AddSeriesException();
        }
    }
}

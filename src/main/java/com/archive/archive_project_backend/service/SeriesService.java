package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddSeriesReqDto;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.exception.FindUserException;
import com.archive.archive_project_backend.exception.add.AddSeriesException;
import com.archive.archive_project_backend.model.SeriesNavigationModel;
import com.archive.archive_project_backend.repository.SeriesMapper;
import com.archive.archive_project_backend.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public SeriesNavigationModel getSeriesNavigationModel(
            String seriesUuid,
            String currentWritingUuid
    ){
        Series series = seriesMapper.getSeriesByUuid(seriesUuid);
        if (series == null || series.getWritings() == null || series.getWritings().isEmpty()) {
            return null;
        }

        // 시리즈 내 글 정렬
        series.writingsSort();

        List<Writing> writings = series.getWritings();

        // 현재 글의 index 찾기
        int currentIndex = -1;
        for (int i = 0; i < writings.size(); i++) {
            if (writings.get(i).getWritingUuid().equals(currentWritingUuid)) {
                currentIndex = i;
                break;
            }
        }

        // 현재 글이 시리즈에 없으면 null
        if (currentIndex == -1) {
            return null;
        }

        // 기본 모델 생성
        SeriesNavigationModel.SeriesNavigationModelBuilder builder =
                SeriesNavigationModel.builder()
                        .seriesUuid(series.getSeriesUuid())
                        .seriesTitle(series.getTitle());

        // 이전 글
        if (currentIndex > 0) {
            Writing prev = writings.get(currentIndex - 1);
            builder
                    .prevWritingUuid(prev.getWritingUuid())
                    .prevWritingTitle(prev.getTitle());
        }

        // 다음 글
        if (currentIndex < writings.size() - 1) {
            Writing next = writings.get(currentIndex + 1);
            builder
                    .nextWritingUuid(next.getWritingUuid())
                    .nextWritingTitle(next.getTitle());
        }

        return builder.build();
    }
}

package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddSeriesReqDto;
import com.archive.archive_project_backend.dto.res.FindSeriesResDto;
import com.archive.archive_project_backend.dto.res.SeriesIndexResDto;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.FindUserException;
import com.archive.archive_project_backend.exception.add.AddSeriesException;
import com.archive.archive_project_backend.model.SeriesNavigationModel;
import com.archive.archive_project_backend.repository.SeriesMapper;
import com.archive.archive_project_backend.repository.UserMapper;
import com.archive.archive_project_backend.repository.WritingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeriesService {
    private final SeriesMapper seriesMapper;
    private final WritingMapper writingMapper;
    private final UserMapper userMapper;

    public FindSeriesResDto getSeriesByUuid(String seriesUuid){
        Series series = seriesMapper.getSeriesByUuid(seriesUuid);
        if(series == null){
            throw new BadRequestException("해당 UUID의 시리즈가 존재하지 않습니다.");
        }

        return FindSeriesResDto.from(series);
    }

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
        List<Writing> writings = writingMapper.selectWritingBySeriesUuid(seriesUuid);
        writings.sort(Comparator.comparingInt(Writing::getSeriesOrder));

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

    public List<SeriesIndexResDto> getSeriesIndexByUserUuid(String uuid){
        List<Series> series = seriesMapper.getSeriesByUserUuid(uuid);
        return series.stream().map(SeriesIndexResDto::from).toList();
    }
}

package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Series;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//series는 null일 수 있음.
@Mapper
public interface SeriesMapper {

    public Series getSeriesByUuid(String uuid);

    public List<Series> getSeriesByUserUuid(String userUuid);

    public int insertSeries(Series series);
    //다음 시리즈 반환
    public int getNextSeriesOrder(String uuid);
}

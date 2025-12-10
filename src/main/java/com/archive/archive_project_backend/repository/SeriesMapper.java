package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Series;
import org.apache.ibatis.annotations.Mapper;

//series는 null일 수 있음.
@Mapper
public interface SeriesMapper {

    public Series getSeriesByUuid(String uuid);
}

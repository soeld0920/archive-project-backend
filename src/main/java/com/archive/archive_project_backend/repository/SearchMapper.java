package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.model.WritingSearchCond;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SearchMapper {

    public List<Writing> searchWriting(WritingSearchCond cond);
    public Integer searchWritingLength(WritingSearchCond cond);
}

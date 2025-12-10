package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    //id로 category 얻는 함수
    public Category getCategoryById(int id);
}

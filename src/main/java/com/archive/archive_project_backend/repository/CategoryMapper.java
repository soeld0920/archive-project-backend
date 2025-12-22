package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.MainCategory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    //id로 category 얻는 함수
    public Category getCategoryById(int id);

    //메인카테고리 리스트를 반환하는 함수
    public List<MainCategory> getMainCategories();

    public List<Category> getCategories(int is);
}

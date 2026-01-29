package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.res.CategoryResDto;
import com.archive.archive_project_backend.dto.res.FindCategoryResDto;
import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.MainCategory;
import com.archive.archive_project_backend.exception.NotFoundException;
import com.archive.archive_project_backend.repository.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryMapper categoryMapper;

    public List<CategoryResDto> getMainCategories(){
        List<MainCategory> categories = categoryMapper.getMainCategories();
        if(categories.isEmpty()){
            throw new NotFoundException("메인 카테고리를 찾지 못했습니다.");
        }
        return categories.stream().map(CategoryResDto::from).toList();
    }

    public List<CategoryResDto> getCategories(int id){
        List<Category> categories = categoryMapper.getCategories(id);
        if(categories.isEmpty()){
            throw new NotFoundException("서브 카테고리를 찾지 못했습니다.");
        }
        return categories.stream().map(CategoryResDto::from).toList();
    }

    public FindCategoryResDto getDetailCategory(int id){
        Category category = categoryMapper.getCategoryById(id);
        return FindCategoryResDto.from(category);
    }

    public CategoryResDto getDetailMainCategory(int id){
        MainCategory mainCategory = categoryMapper.getMainCategoryById(id);
        return CategoryResDto.from(mainCategory);
    }
}

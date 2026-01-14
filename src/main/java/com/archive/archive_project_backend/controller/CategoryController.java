package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.res.CategoryResDto;
import com.archive.archive_project_backend.dto.res.FindCategoryResDto;
import com.archive.archive_project_backend.entity.MainCategory;
import com.archive.archive_project_backend.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/category")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/main")
    public ResponseEntity<List<CategoryResDto>> getMainCategories(){
        List<CategoryResDto> res = categoryService.getMainCategories();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/sub/{id}")
    public ResponseEntity<List<CategoryResDto>> getCategories(@PathVariable int id){
        List<CategoryResDto> res = categoryService.getCategories(id);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/detail/{id}")
    public ResponseEntity<FindCategoryResDto> getDetailCategory(@PathVariable int id){
        FindCategoryResDto dto = categoryService.getDetailCategory(id);
        return ResponseEntity.ok(dto);
    }
}

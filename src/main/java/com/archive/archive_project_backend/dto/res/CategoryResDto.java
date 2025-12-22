package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.MainCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CategoryResDto {
    private int id;
    private String name;

    public static CategoryResDto from(Category c){
        return CategoryResDto.builder().id(c.getCategoryId()).name(c.getSubCategory()).build();
    }

    public static CategoryResDto from(MainCategory c){
        return CategoryResDto.builder().id(c.getMainCategoryId()).name(c.getMainCategory()).build();
    }
}

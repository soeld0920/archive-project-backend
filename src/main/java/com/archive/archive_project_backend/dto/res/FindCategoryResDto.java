package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindCategoryResDto {
    private CategoryResDto mainCategory;
    private CategoryResDto subCategory;

    public static FindCategoryResDto from(Category category){
        return FindCategoryResDto.builder()
                .mainCategory(CategoryResDto.from(category.getMainCategory()))
                .subCategory(CategoryResDto.from(category))
                .build();
    }
}

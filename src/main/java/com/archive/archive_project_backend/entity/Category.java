package com.archive.archive_project_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Category {
    private Integer categoryId;
    private MainCategory mainCategory;
    private String subCategory;
}

package com.archive.archive_project_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WritingSearchCond {
    private Integer mainCategoryId;
    private Integer categoryId;
    private String title;
    private String author;
    private Boolean isSeries;
    private LocalDateTime from;
    private LocalDateTime to;
    private Integer viewMin;
    private Integer viewMax;
    private Integer greatMin;
    private Integer greatMax;
    private Integer limit;
    private Integer offset;
    private Integer sortCode;
}

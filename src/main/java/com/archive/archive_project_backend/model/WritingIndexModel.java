package com.archive.archive_project_backend.model;

import com.archive.archive_project_backend.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WritingIndexModel {
    private String writingUuid;
    private String writingTitle;
    private String authorUuid;
    private String authorName;
    private String seriesUuid;
    private String seriesName;
    private String createAt;
    private String updateAt;
    private int view;
    private int great;
    private int commentCount;
    private String mainCategoryName;
    private String subCategoryName;
    private List<Tag> tags;
    private String image;
    private String content;


}

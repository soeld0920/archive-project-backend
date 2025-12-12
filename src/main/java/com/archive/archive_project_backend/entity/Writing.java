package com.archive.archive_project_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Writing {
    private String writingUuid;
    private String title;
    private String content;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private Category category;
    private User author;
    private int view;
    private int great;
    private List<String> tag;
    //api rule : series가 null이면 단편, 아니면 series 존재
    private Series series;
    private int seriesOrder;
}

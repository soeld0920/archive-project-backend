package com.archive.archive_project_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Comment {
    private int commentId;
    private Writing writing;
    private String content;
    private User author;
    private int writingOrder;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

package com.archive.archive_project_backend.entity;

//todo : 자세히 작성해줘야함

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Series {
    private String seriesUuid;
    private String title;
    private String authorUuid;
    private Category category;
    private int view;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private List<Writing> writings;

    public void writingsSort(){
       writings.sort(Comparator.comparingInt(Writing::getSeriesOrder));
    }
}

package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.Writing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SeriesIndexResDto {
    private String seriesUuid;
    private String title;
    private CategoryResDto category;

    public static SeriesIndexResDto from(Series s){
        return SeriesIndexResDto.builder()
                .seriesUuid(s.getSeriesUuid())
                .title(s.getTitle())
                .category(CategoryResDto.from(s.getCategory()))
                .build();
    }
}

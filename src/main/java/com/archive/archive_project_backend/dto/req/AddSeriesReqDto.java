package com.archive.archive_project_backend.dto.req;

import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.Series;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddSeriesReqDto {
    private String title;
    private Integer categoryId;

    public Series toEntity(String seriesUuid, String authorUuid){
        return Series.builder().
                seriesUuid(seriesUuid)
                .authorUuid(authorUuid)
                .title(title)
                .category(Category.builder().categoryId(categoryId).build())
                .build();
    }
}

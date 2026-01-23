package com.archive.archive_project_backend.dto.res;


import com.archive.archive_project_backend.entity.Series;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindSeriesResDto {
    private String seriesUuid;
    private String seriesTitle;

    public static FindSeriesResDto from(Series series){
        return FindSeriesResDto.builder()
                .seriesUuid(series.getSeriesUuid())
                .seriesTitle(series.getTitle())
                .build();
    }
}

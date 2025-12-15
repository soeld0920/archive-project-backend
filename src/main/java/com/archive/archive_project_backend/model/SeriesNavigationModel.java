package com.archive.archive_project_backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SeriesNavigationModel {
    private String seriesUuid;
    private String seriesTitle;
    private String prevWritingUuid;
    private String prevWritingTitle;
    private String nextWritingUuid;
    private String nextWritingTitle;
}

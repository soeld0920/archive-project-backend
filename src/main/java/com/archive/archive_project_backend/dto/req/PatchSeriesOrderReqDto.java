package com.archive.archive_project_backend.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PatchSeriesOrderReqDto {
    private int index;
    private String writingUuid;
}

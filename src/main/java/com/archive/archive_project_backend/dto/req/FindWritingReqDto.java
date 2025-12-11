package com.archive.archive_project_backend.dto.req;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindWritingReqDto {
    private String writingUuid;
    private String userUuid;
}

package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Writing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindWritingResDto {
    private Writing writing;
    private boolean greated;
    private boolean bookmarked;
}

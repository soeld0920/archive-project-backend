package com.archive.archive_project_backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WritingInteractionStateResDto {
    private boolean greated;
    private boolean bookmarked;
}

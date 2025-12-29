package com.archive.archive_project_backend.entity.textStyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FontFamily {
    private int id;
    private String name;
    private String key;
}

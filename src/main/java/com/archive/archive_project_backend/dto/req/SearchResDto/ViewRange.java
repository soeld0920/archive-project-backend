package com.archive.archive_project_backend.dto.req.SearchResDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ViewRange{
    private int min;
    private int max;
}
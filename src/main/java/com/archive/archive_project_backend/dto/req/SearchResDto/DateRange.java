package com.archive.archive_project_backend.dto.req.SearchResDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DateRange{
    private LocalDateTime from;
    private LocalDateTime to;
};
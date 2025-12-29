package com.archive.archive_project_backend.entity.textStyle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TextStyle {
    private int id;
    private String name;
    private int size;
    private FontFamily font;
    private TextRole role;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikeout;
    private String color;
    private String highlight;
    private String align;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

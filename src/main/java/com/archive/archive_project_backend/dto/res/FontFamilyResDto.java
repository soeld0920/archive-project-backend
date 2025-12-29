package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.textStyle.FontFamily;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FontFamilyResDto {
    private int id;
    private String name;
    private String key;

    public static FontFamilyResDto from(FontFamily fontFamily){
        return FontFamilyResDto.builder()
                .id(fontFamily.getId())
                .name(fontFamily.getName())
                .key(fontFamily.getKey())
                .build();
    }
}

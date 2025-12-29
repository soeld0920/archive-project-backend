package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.textStyle.TextRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TextRoleResDto {
    private String code;
    private String name;

    public static TextRoleResDto from(TextRole tr){
        return TextRoleResDto.builder()
                .code(tr.getCode())
                .name(tr.getName())
                .build();
    }
}

package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.textStyle.TextStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TextStyleResDto {
    private int id;
    private String name;
    private int size;
    private FontFamilyResDto fontFamily;
    private TextRoleResDto textRole;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikeout;
    private String color;
    private String highlight;
    private String align;

    public static TextStyleResDto from(TextStyle ts){
        if (ts == null) {
            return null;
        }

        return TextStyleResDto.builder()
                .id(ts.getId())
                .name(ts.getName())
                .size(ts.getSize())
                .fontFamily(FontFamilyResDto.from(ts.getFont()))
                .textRole(TextRoleResDto.from(ts.getRole()))
                .bold(ts.isBold())
                .italic(ts.isItalic())
                .underline(ts.isUnderline())
                .strikeout(ts.isStrikeout())
                .color(ts.getColor())
                .highlight(ts.getHighlight())
                .align(ts.getAlign())
                .build();
    }

}

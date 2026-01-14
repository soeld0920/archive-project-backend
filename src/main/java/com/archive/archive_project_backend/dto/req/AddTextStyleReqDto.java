package com.archive.archive_project_backend.dto.req;

import com.archive.archive_project_backend.entity.textStyle.FontFamily;
import com.archive.archive_project_backend.entity.textStyle.TextRole;
import com.archive.archive_project_backend.entity.textStyle.TextStyle;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddTextStyleReqDto {
    private String name;
    private int size;

    // 참조값은 id / code만 받는다
    private int fontFamilyId;
    private String textRoleCode;

    private boolean bold;
    private boolean italic;
    private boolean underline;
    private boolean strikeout;

    private String color;
    private String highlight;
    private String align;

    public TextStyle toEntity() {
        TextStyle ts = new TextStyle();

        ts.setName(this.name);
        ts.setSize(this.size);

        // FontFamily (id만 세팅)
        FontFamily font = new FontFamily();
        font.setId(this.fontFamilyId);
        ts.setFont(font);

        // TextRole (code만 세팅)
        TextRole role = new TextRole();
        role.setCode(this.textRoleCode);
        ts.setRole(role);

        ts.setBold(this.bold);
        ts.setItalic(this.italic);
        ts.setUnderline(this.underline);
        ts.setStrikeout(this.strikeout);

        ts.setColor(this.color);
        ts.setHighlight(this.highlight);
        ts.setAlign(this.align);

        return ts;
    }
}

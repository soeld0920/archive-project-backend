package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.textStyle.FontFamily;
import com.archive.archive_project_backend.entity.textStyle.TextRole;
import com.archive.archive_project_backend.entity.textStyle.TextStyle;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TextStyleMapper {

    List<TextStyle> selectTextStylesByUuid(String uuid);
    TextStyle selectTextStyleByUuid(String uuid);
    List<TextRole> getAllTextRoles();
    List<FontFamily> getAllFontFamily();
}

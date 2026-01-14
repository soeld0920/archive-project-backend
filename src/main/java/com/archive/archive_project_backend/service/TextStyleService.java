package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddTextStyleReqDto;
import com.archive.archive_project_backend.dto.res.FontFamilyResDto;
import com.archive.archive_project_backend.dto.res.TextRoleResDto;
import com.archive.archive_project_backend.dto.res.TextStyleResDto;
import com.archive.archive_project_backend.entity.textStyle.FontFamily;
import com.archive.archive_project_backend.entity.textStyle.TextRole;
import com.archive.archive_project_backend.entity.textStyle.TextStyle;
import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.add.AddException;
import com.archive.archive_project_backend.repository.TextStyleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TextStyleService {
    private final TextStyleMapper textStyleMapper;

    public List<TextStyleResDto> getTextStyleByUuid(String uuid){
        List<TextStyle> textStyles = textStyleMapper.selectTextStylesByUuid(uuid);
        if(textStyles == null){
            throw new BadRequestException("해당 유저의 글 스타일이 없습니다.");
        }

        textStyles.sort(Comparator.comparingInt(TextStyle::getId));
        return textStyles.stream().map(TextStyleResDto::from).toList();
    }

    public TextStyleResDto getDefaultTextStyleByUuid(String uuid){
        TextStyle textStyles = textStyleMapper.selectTextStyleByUuid(uuid);
        if(textStyles == null){
            throw new BadRequestException("해당 유저의 글 스타일이 없습니다.");
        }

        return TextStyleResDto.from(textStyles);
    }

    public List<TextRoleResDto> getTextRoles(){
        return textStyleMapper.getAllTextRoles().stream().map(TextRoleResDto::from).toList();
    }

    public List<FontFamilyResDto> getFontFamilies(){
        return textStyleMapper.getAllFontFamily().stream().map(FontFamilyResDto::from).toList();
    }

    public void addTextStyle(AddTextStyleReqDto dto, String userUuid){
        TextStyle ts = dto.toEntity();
        int successCount = textStyleMapper.insertTextStyle(userUuid,ts);
        if(successCount < 1){
            throw new AddException("글자 스타일 추가 중 에러가 발생했습니다.");
        }
    }
}

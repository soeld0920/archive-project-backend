package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.res.FontFamilyResDto;
import com.archive.archive_project_backend.dto.res.TextRoleResDto;
import com.archive.archive_project_backend.dto.res.TextStyleResDto;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.service.TextStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/api/textStyle")
public class TextStyleController {
    private final TextStyleService textStyleService;

    @GetMapping("/me")
    public ResponseEntity<List<TextStyleResDto>> getTextStyle(
            @AuthenticationPrincipal JwtAuthentication auth
            ){
        List<TextStyleResDto> dto = textStyleService.getTextStyleByUuid(auth.getPrincipal());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/me/default")
    public ResponseEntity<TextStyleResDto> getDefaultTextStyle(
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        TextStyleResDto dto = textStyleService.getDefaultTextStyleByUuid(auth.getPrincipal());
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/font")
    public ResponseEntity<List<FontFamilyResDto>> getFontFamilies(){
        List<FontFamilyResDto> res = textStyleService.getFontFamilies();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/role")
    public ResponseEntity<List<TextRoleResDto>> getTextRoles(){
        List<TextRoleResDto> res = textStyleService.getTextRoles();
        return ResponseEntity.ok(res);
    }

}

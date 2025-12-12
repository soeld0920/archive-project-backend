package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.dto.req.FindWritingReqDto;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.jwt.JwtUtil;
import com.archive.archive_project_backend.service.WritingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/writing")
public class WritingController {
    private final WritingService writingService;
    private final JwtUtil jwtUtil;


    @PostMapping("")
    public ResponseEntity<String> addWriting(
            @RequestBody AddWritingReqDto dto,
            @AuthenticationPrincipal JwtAuthentication auth
            ){
        writingService.addWriting(dto, auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).body("성공적으로 글이 생성되었습니다.");
    }

    @GetMapping("/{writingUuid}")
    public ResponseEntity<FindWritingResDto> findWriting(
            @PathVariable String writingUuid,
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        FindWritingReqDto reqDto = new FindWritingReqDto(writingUuid, auth.getPrincipal());
        FindWritingResDto resDto = writingService.findWritingByUuid(reqDto);
        return ResponseEntity.ok(resDto);
    }
}

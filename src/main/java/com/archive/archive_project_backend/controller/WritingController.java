package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.service.WritingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/writing")
public class WritingController {
    private final WritingService writingService;

    @PostMapping("")
    public ResponseEntity<String> addWriting(@RequestBody AddWritingReqDto dto){
        writingService.addWriting(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("성공적으로 글이 생성되었습니다.");
    }
}

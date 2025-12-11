package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.dto.req.FindWritingReqDto;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.service.WritingService;
import jakarta.servlet.http.HttpServletResponse;
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

    @GetMapping("/{writingUuid}")
    public ResponseEntity<FindWritingResDto> findWriting(
            @PathVariable String writingUuid,
            HttpServletResponse response
    ){
        String userUuid = response.getHeader("sub");
        FindWritingReqDto reqDto = new FindWritingReqDto(writingUuid, userUuid);
        FindWritingResDto resDto = writingService.findWritingByUuid(reqDto);
        return ResponseEntity.ok(resDto);
    }
}

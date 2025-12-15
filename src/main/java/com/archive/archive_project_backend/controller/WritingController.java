package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddWritingReqDto;
import com.archive.archive_project_backend.dto.req.FindWritingReqDto;
import com.archive.archive_project_backend.dto.req.ToggleReqDto;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.dto.res.WritingInteractionStateResDto;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.jwt.JwtUtil;
import com.archive.archive_project_backend.service.WritingService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/writing")
@Slf4j
public class WritingController {
    private final WritingService writingService;

    private String getJwtAuthenticationPrincipal(JwtAuthentication auth){
        return auth != null ? auth.getPrincipal() : null;
    }

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
        String userUuid = getJwtAuthenticationPrincipal(auth);
        FindWritingReqDto reqDto = new FindWritingReqDto(writingUuid, userUuid);
        FindWritingResDto resDto = writingService.findWritingByUuid(reqDto);
        return ResponseEntity.ok(resDto);
    }

    @GetMapping("/{writingUuid}/interactionState")
    public ResponseEntity<WritingInteractionStateResDto> getWritingInteractionState(
            @PathVariable String writingUuid,
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        String userUuid = getJwtAuthenticationPrincipal(auth);
        WritingInteractionStateResDto dto = writingService.getWritingInteractionState(writingUuid, userUuid);
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{writingUuid}/great")
    public ResponseEntity<Void> greatWriting(
            @PathVariable String writingUuid,
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ToggleReqDto dto
            ){
        String userUuid = getJwtAuthenticationPrincipal(auth);
        if(dto.isNext()){
            writingService.greatedWriting(writingUuid, userUuid);
        }else{
            writingService.ungreatedWriting(writingUuid, userUuid);
        }

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{writingUuid}/bookmark")
    public ResponseEntity<Void> bookmarkWriting(
            @PathVariable String writingUuid,
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestBody ToggleReqDto dto
    ){
        String userUuid = getJwtAuthenticationPrincipal(auth);
        if(dto.isNext()){
            writingService.bookmarkedWriting(writingUuid, userUuid);
        }else{
            writingService.unbookmarkedWriting(writingUuid, userUuid);
        }

        return ResponseEntity.noContent().build();
    }
}

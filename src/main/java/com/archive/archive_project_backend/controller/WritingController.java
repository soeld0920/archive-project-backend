package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.*;
import com.archive.archive_project_backend.dto.res.FindWritingResDto;
import com.archive.archive_project_backend.dto.res.WritingInteractionStateResDto;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.model.WritingIndexModel;
import com.archive.archive_project_backend.service.writing.WritingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

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
            ) throws IOException {
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

    @GetMapping("/bySeries/{seriesUuid}")
    public ResponseEntity<List<WritingIndexModel>> getWritingIndexBySeriesUuid(@PathVariable String seriesUuid){
        List<WritingIndexModel> res = writingService.getWritingIndexBySeriesUuid(seriesUuid);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/byUser/{authorUuid}")
    public ResponseEntity<List<WritingIndexModel>> getWritingIndexByAuthorUuid(@PathVariable String authorUuid){
        List<WritingIndexModel> res = writingService.getWritingIndexByAuthorUuid(authorUuid);
        return ResponseEntity.ok(res);
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

    @PostMapping("/{writingUuid}/view")
    public ResponseEntity<Void> increaseView(
            @PathVariable String writingUuid
    ){
        writingService.increaseView(writingUuid);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{writingUuid}")
    public ResponseEntity<Void> deleteWriting(
            @PathVariable String writingUuid,
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        writingService.deleteWriting(writingUuid, auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/{writingUuid}")
    public ResponseEntity<Void> patchWriting(
            @PathVariable String writingUuid,
            @RequestBody PatchWritingReqDto dto,
            @AuthenticationPrincipal JwtAuthentication auth
    ) throws IOException {
        String userUuid = auth.getPrincipal();
        writingService.patchWriting(writingUuid, userUuid, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/seriesOrder/{seriesUuid}")
    public ResponseEntity<Void> patchSeriesOrder(
            @PathVariable String seriesUuid,
            @RequestBody List<PatchSeriesOrderReqDto> dtos
            ){
        writingService.patchSeriesOrder(seriesUuid, dtos);
        return ResponseEntity.ok().build();
    }
}

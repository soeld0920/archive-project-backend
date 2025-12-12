package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddSeriesReqDto;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/series")
public class SeriesController {
    private final SeriesService seriesService;

    @PostMapping("")
    public ResponseEntity<String> addSeries(
            @RequestBody AddSeriesReqDto dto,
            @AuthenticationPrincipal JwtAuthentication auth
            ){
        String authorUuid = auth.getPrincipal();
        seriesService.addSeries(dto, authorUuid);
        return ResponseEntity.status(HttpStatus.CREATED).body("생성되었습니다.");
    }
}

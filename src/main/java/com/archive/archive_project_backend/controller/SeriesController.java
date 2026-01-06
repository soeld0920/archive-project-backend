package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddSeriesReqDto;
import com.archive.archive_project_backend.dto.res.SeriesIndexResDto;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.model.SeriesNavigationModel;
import com.archive.archive_project_backend.service.SeriesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{seriesUuid}/navigation")
    public ResponseEntity<SeriesNavigationModel> getSeriesNavigationModel(
            @PathVariable String seriesUuid,
            @RequestParam String currentWritingUuid
    ){
        SeriesNavigationModel model = seriesService.getSeriesNavigationModel(seriesUuid, currentWritingUuid);
        if(model == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(model);
    }

    @GetMapping("/me/index")
    public ResponseEntity<List<SeriesIndexResDto>> getSeriesIndexes(
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        String uuid = auth.getPrincipal();
        List<SeriesIndexResDto> resDtos = seriesService.getSeriesIndexByUserUuid(uuid);
        return ResponseEntity.ok(resDtos);
    }
}

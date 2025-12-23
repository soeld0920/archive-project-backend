package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.SearchResDto.SearchReqDto;
import com.archive.archive_project_backend.model.WritingIndexModel;
import com.archive.archive_project_backend.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//todo : 정렬기준 만족시켜야함
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/search")
public class SearchController {
    private final SearchService searchService;

    @PostMapping("")
    public ResponseEntity<List<WritingIndexModel>> SearchWriting(
            @RequestBody SearchReqDto dto
            ){
        List<WritingIndexModel> writingIndexes = searchService.searchWriting(dto);
        return ResponseEntity.ok(writingIndexes);
    }

    @PostMapping("/length")
    public ResponseEntity<Integer> SearchWritingLength(
            @RequestBody SearchReqDto dto
    ){
        int res = searchService.searchWritingLength(dto);
        return ResponseEntity.ok(res);
    }
}

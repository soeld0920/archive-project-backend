package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.SearchResDto.SearchReqDto;
import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.model.WritingIndexModel;
import com.archive.archive_project_backend.model.WritingSearchCond;
import com.archive.archive_project_backend.repository.SearchMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final SearchMapper searchMapper;
    private final ObjectMapper objectMapper;

    public List<WritingIndexModel> searchWriting(SearchReqDto dto){
        //dto -> cond
        WritingSearchCond cond = dto.toCond();
        List<Writing> writings = searchMapper.searchWriting(cond);
        return writings.stream().map(WritingIndexModel::from).toList();
    }

    public Integer searchWritingLength(SearchReqDto dto){
        WritingSearchCond cond = dto.toCond();
        return searchMapper.searchWritingLength(cond);
    }
}

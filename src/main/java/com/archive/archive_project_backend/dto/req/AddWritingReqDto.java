package com.archive.archive_project_backend.dto.req;

import com.archive.archive_project_backend.entity.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

//todo : 값 검증
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AddWritingReqDto {

    @NotBlank(message = "제목은 비어있을 수 없습니다.")
    @Size(max = 100, message = "제목은 100자 이하여야합니다.")
    private String title;

    @NotBlank(message = "내용은 비어있을 수 없습니다.")
    private JsonNode content;

    private int categoryId;
    private List<String> tag;
    //api rule : series가 null이면 단편, 아니면 series 존재
    private String seriesUuid;

    public Writing toEntity(String uuid, User author, Category category, Series series, ObjectMapper objectMapper){
        String contentJson;
        try {
            contentJson = objectMapper.writeValueAsString(content); // ⭐ 여기!
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("content를 JSON 문자열로 변환 실패", e);
        }

        Writing writing = Writing.builder()
                .writingUuid(uuid)
                .title(this.getTitle())
                .content(contentJson)
                .author(author)
                .tag(Tag.asStrings(tag))
                .series(series)
                .category(category)
                .build();

        return writing;
    }
}

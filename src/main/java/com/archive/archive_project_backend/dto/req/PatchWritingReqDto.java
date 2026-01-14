package com.archive.archive_project_backend.dto.req;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PatchWritingReqDto {
    @Size(max = 100, message = "제목은 100자 이하여야합니다.")
    private String title;
    private JsonNode content;
    private Integer categoryId;
    private List<String> tag;
    private String seriesUuid;
}

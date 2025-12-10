package com.archive.archive_project_backend.dto.req;

import com.archive.archive_project_backend.entity.Category;
import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
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
    private String title;
    private String content;
    private int categoryId;
    private String authorUuid;
    private List<String> tag;
    //api rule : series가 null이면 단편, 아니면 series 존재
    private String seriesUuid;

    public Writing toEntity(String uuid, User author, Category category, Series series){
        Writing writing = Writing.builder()
                .writingUuid(uuid)
                .title(this.getTitle())
                .content(this.getContent())
                .author(author)
                .tag(this.getTag())
                .series(series)
                .category(category)
                .build();

        return writing;
    }
}

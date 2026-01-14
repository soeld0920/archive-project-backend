package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.Tag;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Slf4j
public class FindWritingResDto {
    private String writingUuid;
    private String writingTitle;
    private JsonNode content;
    private int view;
    private int great;
    private int commentCount;
    private int categoryId;
    private String mainCategoryName;
    private String subCategoryName;
    private String authorUuid;
    private String authorName;
    private String seriesUuid;
    private String seriesName;
    private int seriesOrder;
    private List<Tag> tag;
    private LocalDateTime updateAt;
    private LocalDateTime createAt;

    public static FindWritingResDto from(Writing writing, ObjectMapper objectMapper) {
        User author = writing.getAuthor();
        Series series = writing.getSeries();

        JsonNode contentNode = null;
        try {
            String content = writing.getContent(); // content가 String이라고 가정
            contentNode = (content == null || content.isBlank())
                    ? null
                    : objectMapper.readTree(content);
        } catch (Exception e) {
            // content가 깨진 JSON이면 여기서 처리 전략 선택
            // 1) null로 보내기
            contentNode = null;
            // 2) 또는 예외 던지기(권장: 데이터가 항상 정상이어야 한다면)
            // throw new IllegalArgumentException("Invalid JSON in writing.content", e);
        }
        return FindWritingResDto.builder()
                .writingUuid(writing.getWritingUuid())
                .writingTitle(writing.getTitle())
                .content(contentNode)
                .view(writing.getView())
                .great(writing.getGreat())
                .commentCount(writing.getCommentCount())
                .categoryId(writing.getCategory().getCategoryId())
                .mainCategoryName(writing.getCategory().getMainCategory().getMainCategory())
                .subCategoryName(writing.getCategory().getSubCategory())
                .authorUuid(author != null ? author.getUserUuid() : null)
                .authorName(author != null ? author.getName() : "회원탈퇴한 유저")
                .seriesUuid(series != null ? series.getSeriesUuid() : null)
                .seriesName(series != null ? series.getTitle() : null)
                .seriesOrder(writing.getSeriesOrder())
                .tag(writing.getTag())
                .createAt(writing.getCreateAt())
                .updateAt(writing.getUpdateAt())
                .build();
    }
}

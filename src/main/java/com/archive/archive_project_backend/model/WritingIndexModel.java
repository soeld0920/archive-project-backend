package com.archive.archive_project_backend.model;

import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.Tag;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jayway.jsonpath.JsonPath;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class WritingIndexModel {
    private String writingUuid;
    private String writingTitle;
    private String authorUuid;
    private String authorImage;
    private String authorName;
    private String seriesUuid;
    private String seriesName;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int view;
    private int great;
    private int commentCount;
    private String mainCategoryName;
    private String subCategoryName;
    private List<Tag> tags;
    private String image;
    private String content;

    public static WritingIndexModel from(Writing w) {
        String cover = null;
        String preview = null;

        try {
            String writingContent = w.getContent();
            if (writingContent != null && !writingContent.isBlank()) {

                // 1) 대표 이미지
                List<String> images = JsonPath.read(
                        writingContent,
                        "$..[?(@.type=='image')].attrs.src"
                );
                cover = (images == null || images.isEmpty()) ? null : images.get(0);

                // 혹시 image type이 다르면 fallback
                if (cover == null) {
                    List<String> srcs = JsonPath.read(writingContent, "$..attrs.src");
                    if (srcs != null) {
                        cover = srcs.stream()
                                .filter(s -> s != null && (s.startsWith("/files/") || s.startsWith("http")))
                                .findFirst()
                                .orElse(null);
                    }
                }

                // 2) 미리보기 텍스트 150자
                List<String> texts = JsonPath.read(
                        writingContent,
                        "$..[?(@.type=='text')].text"
                );
                String textJoin = (texts == null) ? "" : String.join("", texts);
                preview = textJoin.substring(0, Math.min(150, textJoin.length())).trim();
            }
        } catch (Exception e) {
            // 필요하면 로그 찍기
            // log.warn("index extract failed, writingUuid={}", w.getWritingUuid(), e);
        }

        User author = w.getAuthor();
        Series series = w.getSeries();

        // 배너는 본문이 아니라 author에서 가져오는 게 정상
        String banner = (author != null) ? author.getBanner() : null;

        return WritingIndexModel.builder()
                .writingUuid(w.getWritingUuid())
                .writingTitle(w.getTitle())
                .authorUuid(author != null ? author.getUserUuid() : null)
                .authorName(author != null ? author.getName() : "회원탈퇴한 유저")
                .authorImage(author != null ? author.getBanner() : null)
                .seriesName(series != null ? series.getTitle() : null)
                .seriesUuid(series != null ? series.getSeriesUuid() : null)
                .view(w.getView())
                .great(w.getGreat())
                .commentCount(w.getCommentCount())
                .mainCategoryName(w.getCategory().getMainCategory().getMainCategory())
                .subCategoryName(w.getCategory().getSubCategory())
                .tags(w.getTag())
                .image(cover)
                .content(preview)
                .createAt(w.getCreateAt())
                .updateAt(w.getUpdateAt())
                .build();
    }
}

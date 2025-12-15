package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Series;
import com.archive.archive_project_backend.entity.Tag;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindWritingResDto {
    private String writingUuid;
    private String writingTitle;
    private String content;
    private int view;
    private int great;
    private int commentCount;
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

    public static FindWritingResDto from(Writing writing){
        User author = writing.getAuthor();
        Series series = writing.getSeries();
        return FindWritingResDto.builder()
                // writing
                .writingUuid(writing.getWritingUuid())
                .writingTitle(writing.getTitle())
                .content(writing.getContent())
                .view(writing.getView())
                .great(writing.getGreat())
                .commentCount(writing.getCommentCount())
                .mainCategoryName(writing.getCategory().getMainCategory())
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

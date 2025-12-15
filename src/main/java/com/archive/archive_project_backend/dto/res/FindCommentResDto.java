package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.Comment;
import com.archive.archive_project_backend.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindCommentResDto {
    private int order;
    private String content;
    private String userUuid;
    private String userName;
    private String userBanner;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public static FindCommentResDto from(Comment comment){
        User author = comment.getAuthor();
        return FindCommentResDto.builder()
                .order(comment.getWritingOrder())
                .content(comment.getContent())
                .userUuid(author.getUserUuid())
                .userName(author.getName())
                .userBanner(author.getBanner())
                .createAt(comment.getCreateAt())
                .updateAt(comment.getUpdateAt())
                .build();
    }
}

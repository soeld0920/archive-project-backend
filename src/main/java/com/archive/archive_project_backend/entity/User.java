package com.archive.archive_project_backend.entity;

//todo : 글 작성을 위한 최소한의 entity임.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private String userUuid;
    private String name;
    private String bio;
    private String email;
    private String banner;
    private Role role;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private int totalWriting;
    private int totalView;
    private int totalGreat;
    private int totalComment;
}

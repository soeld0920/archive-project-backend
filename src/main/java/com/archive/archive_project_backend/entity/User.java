package com.archive.archive_project_backend.entity;

//todo : 글 작성을 위한 최소한의 entity임.

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class User {
    private String userUuid;
}

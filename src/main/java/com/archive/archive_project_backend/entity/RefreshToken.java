package com.archive.archive_project_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RefreshToken {
    private int refreshTokenId;
    private int userLoginId;
    private String token;
    private LocalDateTime expireAt;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
}

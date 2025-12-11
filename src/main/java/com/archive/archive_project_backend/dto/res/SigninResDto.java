package com.archive.archive_project_backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SigninResDto {
    private String accessToken;
    private String refreshToken;
}

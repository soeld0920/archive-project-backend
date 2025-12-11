package com.archive.archive_project_backend.dto.req;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SigninReqDto {
    private String userid;
    private String passwd;
}

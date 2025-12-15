package com.archive.archive_project_backend.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FindUserResDto {
    private String userUuid;
    private String userName;
    private String bio;
    private String banner;
    private String roleName;
}

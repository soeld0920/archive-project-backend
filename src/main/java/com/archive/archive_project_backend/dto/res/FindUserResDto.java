package com.archive.archive_project_backend.dto.res;

import com.archive.archive_project_backend.entity.User;
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
    private int totalWriting;
    private int totalView;
    private int totalGreat;
    private int totalComment;

    public static FindUserResDto from(User user){
        return FindUserResDto.builder()
                .userUuid(user.getUserUuid())
                .userName(user.getName())
                .bio(user.getBio())
                .banner(user.getBanner())
                .roleName(user.getRole().getRoleName())
                .totalWriting(user.getTotalWriting())
                .totalView(user.getTotalView())
                .totalGreat(user.getTotalGreat())
                .totalComment(user.getTotalComment())
                .build();
    }
}

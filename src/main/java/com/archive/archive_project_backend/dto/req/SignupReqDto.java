package com.archive.archive_project_backend.dto.req;

import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.UserLogin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SignupReqDto {

    @Size(min = 4, max = 20, message = "아이디는 4자 이상 20자 이하여야합니다.")
    private String userid;

    @Size(min = 8, max = 20, message = "비밀번호는 8자 이상 20자 이하여야합니다.")
    private String passwd;

    @NotBlank(message = "이름을 비울 수 없습니다.")
    @Size(max = 20, message = "이름은 20자 이하여야합니다.")
    private String name;
    private String bio;

    @Email
    private String email;
    private String banner;

    public User toUserInfo(String uuid){
        return User.builder()
                .userUuid(uuid)
                .name(name)
                .bio(bio)
                .email(email)
                .banner(banner)
                .build();
    }

    public UserLogin toUserLogin(String uuid){
        return UserLogin.builder()
                .userUuid(uuid)
                .userid(userid)
                .passwd(passwd)
                .build();
    }
}

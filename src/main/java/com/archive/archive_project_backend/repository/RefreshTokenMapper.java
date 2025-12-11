package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.RefreshToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

@Mapper
public interface RefreshTokenMapper {

    //추가
    int insertRefreshToken(
            @Param("userLoginId") int userLoginId,
            @Param("token") String token,
            @Param("expireAt") LocalDateTime expireAt
    );

    //조회
    RefreshToken getRefreshTokenByToken(String token);

    //삭제
    int deleteRefreshTokenById(int id);
}

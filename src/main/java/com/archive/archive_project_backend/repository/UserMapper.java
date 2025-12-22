package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.UserLogin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

    //유틸
    public User getUserByUuid(String uuid);
    public User getUserByUserid(String userId);
    public UserLogin getUserLoginById(int id);

    //유저 추가
    public int insertUserInfo(User userInfo);
    public int insertUserLogin(UserLogin userLogin);

    //login 정보 가져오기
    public UserLogin selectUserLoginByUserid(String userid);

    //존재 여부 반환
    public boolean existsUserByUuid(String uuid);

    //메타데이터 변환
    public int updateTotalViewDelta(
            @Param("userUuid") String uuid,
            @Param("delta")int delta
    );
    public int updateTotalGreatDelta(
            @Param("userUuid") String uuid,
            @Param("delta")int delta
    );
    public int updateTotalWritingDelta(
            @Param("userUuid") String uuid,
            @Param("delta")int delta
    );
    public int updateTotalCommentDelta(
            @Param("userUuid") String uuid,
            @Param("delta")int delta
    );
}

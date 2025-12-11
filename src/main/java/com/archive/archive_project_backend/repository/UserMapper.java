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

    //유저 추가
    public int insertUserInfo(User userInfo);
    public int insertUserLogin(UserLogin userLogin);
}

package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    public User getUserByUuid(String uuid);
}

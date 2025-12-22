package com.archive.archive_project_backend.repository;

import com.archive.archive_project_backend.entity.Tag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface TagMapper {

    public int insertTags(@Param("tags") List<Tag> tags);

    public List<Tag> getTagsByTagNames(@Param("tags") List<Tag> tags);

    public int insertWritingTags(
            @Param("writingUuid") String writingUuid,
            @Param("tags") List<Tag> tags
            );

    public List<Tag> selectTagsByWritingUuid(String writingUuid);

    public int deleteWritingTag(String writingUuid);
}

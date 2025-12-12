package com.archive.archive_project_backend.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Tag {
    private Integer tagId;
    private String tagName;

    /*
    주의 : id는 비어있음
     */
    public static Tag asString(String tagStr){
        return Tag.builder().tagName(tagStr).build();
    }

    public static List<Tag> asStrings(List<String> list){
        return list.stream().map(Tag::asString).collect(Collectors.toList());
    }

    public boolean equals(Tag t){
        return this.tagName.equals(t.tagName);
    }
}

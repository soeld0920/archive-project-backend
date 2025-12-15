package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.AddCommentReqDto;
import com.archive.archive_project_backend.dto.res.FindCommentResDto;
import com.archive.archive_project_backend.entity.Comment;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.Writing;
import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.add.AddCommentException;
import com.archive.archive_project_backend.repository.CommentMapper;
import com.archive.archive_project_backend.repository.WritingMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentMapper commentMapper;
    private final WritingMapper writingMapper;

    public void addComment(String writingUuid, AddCommentReqDto dto, String authorUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글이 존재하지 않습니다.");
        }

        int nextOrder = commentMapper.getNextCommentOrder(writingUuid);

        //빌드
        Comment comment = Comment.builder()
                .writing(Writing.builder().writingUuid(writingUuid).build())
                .content(dto.getContent())
                .author(User.builder().userUuid(authorUuid).build())
                .writingOrder(nextOrder)
                .build();

        int successCount = commentMapper.insertComment(comment);
        if(successCount < 1){
            throw new AddCommentException();
        }
    }

    public List<FindCommentResDto> findComments(String writingUuid){
        if(!writingMapper.existsWritingByUuid(writingUuid)){
            throw new BadRequestException("해당 글이 존재하지 않습니다.");
        }
        List<Comment> comments = commentMapper.selectCommentsByWritingUuid(writingUuid);
        List<FindCommentResDto> dtos = comments.stream().map(FindCommentResDto::from).collect(Collectors.toList());
        dtos.sort(Comparator.comparingInt(FindCommentResDto::getOrder));
        return dtos;
    }
}

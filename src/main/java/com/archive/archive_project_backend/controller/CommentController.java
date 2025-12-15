package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.AddCommentReqDto;
import com.archive.archive_project_backend.dto.res.FindCommentResDto;
import com.archive.archive_project_backend.exception.login.NotLoginUserException;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import com.archive.archive_project_backend.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/writing/{writingUuid}/comment")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("")
    public ResponseEntity<Void> addComment(
            @PathVariable String writingUuid,
            @RequestBody AddCommentReqDto dto,
            @AuthenticationPrincipal JwtAuthentication auth
    ){
        commentService.addComment(writingUuid, dto, auth.getPrincipal());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("")
    public  ResponseEntity<List<FindCommentResDto>> findComment(
            @PathVariable String writingUuid
    ){
        List<FindCommentResDto> dtos = commentService.findComments(writingUuid);
        return ResponseEntity.ok(dtos);
    }
}

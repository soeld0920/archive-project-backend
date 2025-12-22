package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.infra.storage.LocalFileStorage;
import com.archive.archive_project_backend.jwt.JwtAuthentication;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
@Slf4j
public class FileController {
    private final LocalFileStorage localFileStorage;

    @PostMapping("/")
    public ResponseEntity<Map<String, String>> uploadTmp(
            @AuthenticationPrincipal JwtAuthentication auth,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        //비로그인 제한 -> auth로 자동으로 완성
        var stored = localFileStorage.storeTmp(file);

        return ResponseEntity.ok(Map.of("url", stored.url()));
    }
}

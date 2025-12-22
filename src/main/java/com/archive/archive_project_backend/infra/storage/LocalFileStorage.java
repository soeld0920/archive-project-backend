package com.archive.archive_project_backend.infra.storage;

import com.archive.archive_project_backend.exception.BadRequestException;
import com.archive.archive_project_backend.exception.file.EmptyFileException;
import com.archive.archive_project_backend.exception.file.InvalidFileException;
import com.archive.archive_project_backend.exception.file.PathTraversalException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.archive.archive_project_backend.constants.FileConstants.DIRECTORY;
import static com.archive.archive_project_backend.constants.FileConstants.ROOT_TMP;

@Service
@RequiredArgsConstructor
public class LocalFileStorage {
    private static final Set<String> ALLOWED_IMAGE_FILE = Set.of("image/jpg", "image/jpeg", "image/png", "image/webp");
    private static final Pattern TEMP_FILE_PATTERN =
            Pattern.compile("^[0-9a-fA-F-]{36}\\.(png|jpg|jpeg|webp)$");

    @Value("${app.upload.root}")
    private String uploadRoot;

    public StoredFile storeTmp(MultipartFile file) throws IOException {
        //가드
        if(file == null || file.isEmpty()){
            throw new EmptyFileException();
        }
        if(!ALLOWED_IMAGE_FILE.contains(file.getContentType())){
            throw new InvalidFileException();
        }

        //파일명 결정
        String ext = switch (file.getContentType()) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png" -> "png";
            case "image/webp" -> "webp";
            default -> "bin";
        };
        String filename = UUID.randomUUID() + "." + ext;

        //저장경로 결정
        Path dir = Paths.get(uploadRoot, "tmp").toAbsolutePath().normalize();

        Path target = dir.resolve(filename).normalize();

        //보안
        if(!target.startsWith(dir)){
            throw new PathTraversalException();
        }

        //저장
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        //url
        String publicUrl = ROOT_TMP + filename;

        return new StoredFile(publicUrl, target.toString());
    }

    //파일의 위치를 옮기는 함수
    public String moveFromTmp(String tempKey, String targetDir) throws IOException {
        //가드
        if(tempKey == null || !TEMP_FILE_PATTERN.matcher(tempKey).matches()){
            throw new BadRequestException("잘못된 url입니다.");
        }

        if(!DIRECTORY.contains(targetDir.split("/")[0])){
            throw new BadRequestException("잘못된 디렉토리입니다.");
        }

        Path tmpDir = Paths.get(uploadRoot, "tmp").toAbsolutePath().normalize();
        Path src = tmpDir.resolve(tempKey).normalize();

        if(!src.startsWith(tmpDir)){
            throw new PathTraversalException();
        }

        //존재 확인
        if (!Files.exists(src) || !Files.isRegularFile(src)) {
            throw new BadRequestException("Temp file not found");
        }

        //목적지 확인
        Path destDir = Paths.get(uploadRoot, targetDir).toAbsolutePath().normalize();
        if(!destDir.startsWith(uploadRoot)) throw new PathTraversalException();
        Files.createDirectories(destDir);

        Path dest = destDir.resolve(tempKey).normalize();
        if (!dest.startsWith(destDir)) {
            throw new PathTraversalException();
        }

        Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);

        //url
        return "/files/" + targetDir.replace("\\", "/") + "/" + tempKey;
    }

    public record StoredFile(String url, String savedPath) {}
}

package com.archive.archive_project_backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {
    @Value("${app.upload.root}")
    private String uploadRoot;

    //파일 접근용
    //localhost:8080/files로 정적 소스에 접근 가능
    public void addResourceHandler(ResourceHandlerRegistry registry){
        Path root = Paths.get(uploadRoot).toAbsolutePath().normalize();
        String location = root.toUri().toString(); //file:///C:...

        registry.addResourceHandler("/files/**")
                .addResourceLocations(location);
    }
}

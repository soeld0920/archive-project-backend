package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.SigninReqDto;
import com.archive.archive_project_backend.dto.req.SignupReqDto;
import com.archive.archive_project_backend.dto.res.FindUserResDto;
import com.archive.archive_project_backend.dto.res.SigninResDto;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.exception.login.RefreshTokenException;
import com.archive.archive_project_backend.service.LoginService;
import com.archive.archive_project_backend.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userUuid}")
    public ResponseEntity<FindUserResDto> getUser(@PathVariable String userUuid){
        FindUserResDto user = userService.getuser(userUuid);
        if(user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }
}

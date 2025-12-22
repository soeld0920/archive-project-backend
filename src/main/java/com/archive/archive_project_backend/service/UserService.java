package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.SigninReqDto;
import com.archive.archive_project_backend.dto.req.SignupReqDto;
import com.archive.archive_project_backend.dto.res.FindUserResDto;
import com.archive.archive_project_backend.dto.res.SigninResDto;
import com.archive.archive_project_backend.entity.RefreshToken;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.UserLogin;
import com.archive.archive_project_backend.exception.FindUserException;
import com.archive.archive_project_backend.exception.login.RefreshTokenException;
import com.archive.archive_project_backend.exception.login.SigninException;
import com.archive.archive_project_backend.exception.login.SignupException;
import com.archive.archive_project_backend.jwt.JwtUtil;
import com.archive.archive_project_backend.repository.RefreshTokenMapper;
import com.archive.archive_project_backend.repository.UserMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserMapper userMapper;

    //get
    public FindUserResDto getuser(String userUuid){
        User user = userMapper.getUserByUuid(userUuid);
        if(user == null){
            return null;
        }

        return FindUserResDto.from(user);
    }
}

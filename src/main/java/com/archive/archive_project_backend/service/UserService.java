package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.dto.req.SignupReqDto;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.UserLogin;
import com.archive.archive_project_backend.exception.login.SignupException;
import com.archive.archive_project_backend.jwt.JwtUtil;
import com.archive.archive_project_backend.repository.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;

    //회원가입
    @Transactional(rollbackFor = Exception.class)
    public void signup(SignupReqDto dto){
        //id 중복 체크
        if(userMapper.getUserByUserid(dto.getUserid()) != null){
            throw new SignupException("이미 존재하는 아이디입니다.", HttpStatus.CONFLICT);
        }

        //dto -> entity
        String uuid = UUID.randomUUID().toString();

        User user = dto.toUserInfo(uuid);
        UserLogin userLogin = dto.toUserLogin(uuid);

        //비번 암호화
        userLogin.setPasswd(encoder.encode(dto.getPasswd()));

        //유저 정보 저장부터
        int successCount1 = userMapper.insertUserInfo(user);
        int successCount2 = userMapper.insertUserLogin(userLogin);
        if(successCount1 < 1 || successCount2 < 1){
            throw new SignupException("추가 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

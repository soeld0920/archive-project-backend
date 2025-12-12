package com.archive.archive_project_backend.controller;

import com.archive.archive_project_backend.dto.req.SigninReqDto;
import com.archive.archive_project_backend.dto.req.SignupReqDto;
import com.archive.archive_project_backend.dto.res.SigninResDto;
import com.archive.archive_project_backend.exception.login.RefreshTokenException;
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

    //refresh 토큰 쿠키 저장
    private void addRefreshTokenCookie(
            String refreshToken,
            HttpServletResponse response
    ){
        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true) // 실제 운영시에는 true해야함. JS로 조작을 막는 역할
                .secure(true) // 실제 운영시 true, http 프로토컬만 가능하게
                .sameSite("Lax") // csrf 정책 - get 요청 허용
                .path("/")
                .maxAge(-1) // 유효기간, -1은 세션 종료시 쿠키도 삭제
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signup(
            @RequestBody @Valid SignupReqDto dto
    ){
        userService.signup(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원가입에 성공하였습니다.");
    }

    @PostMapping("/signin")
    public ResponseEntity<SigninResDto> signin(
            @RequestBody SigninReqDto reqDto,
            HttpServletResponse response
    ){
        SigninResDto resDto = userService.signin(reqDto);

        //쿠키에 refresh 저장
        addRefreshTokenCookie(resDto.getRefreshToken(), response);

        return ResponseEntity.ok(resDto);
    }

    @PostMapping("/refresh")
    public ResponseEntity<SigninResDto> refresh(
            HttpServletResponse response,
            @CookieValue(value = "refreshToken", required = false) String refreshToken
    ){
        if(refreshToken == null){
            throw new RefreshTokenException("refreshToken이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        SigninResDto dto = userService.refresh(refreshToken);
        addRefreshTokenCookie(dto.getRefreshToken(),response);
        return ResponseEntity.ok(dto);
    }
}

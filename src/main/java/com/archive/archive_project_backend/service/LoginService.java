package com.archive.archive_project_backend.service;

import com.archive.archive_project_backend.constants.FileConstants;
import com.archive.archive_project_backend.dto.req.SigninReqDto;
import com.archive.archive_project_backend.dto.req.SignupReqDto;
import com.archive.archive_project_backend.dto.res.SigninResDto;
import com.archive.archive_project_backend.entity.RefreshToken;
import com.archive.archive_project_backend.entity.User;
import com.archive.archive_project_backend.entity.UserLogin;
import com.archive.archive_project_backend.exception.login.RefreshTokenException;
import com.archive.archive_project_backend.exception.login.SigninException;
import com.archive.archive_project_backend.exception.login.SignupException;
import com.archive.archive_project_backend.infra.storage.LocalFileStorage;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginService {
    private final UserMapper userMapper;
    private final RefreshTokenMapper refreshTokenMapper;
    private final BCryptPasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final LocalFileStorage localFileStorage;

    @Value("${jwt.refresh-expire-millis}")
    private long refreshExpireMillis;

    //토큰 생성기
    private SigninResDto generateTokenPair(User user){
        Map<String, Object> claims = Map.of(
                "role", user.getRole().getRoleName()
        );
        String accessToken = jwtUtil.generateAccessToken(user.getUserUuid(), claims);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUserUuid(), claims);

        return new SigninResDto(accessToken, refreshToken);
    }

    //refresh 토큰 저장기
    private void saveRefreshToken(UserLogin userLogin, String refreshToken){
        LocalDateTime expiredAt = LocalDateTime.now().plus(refreshExpireMillis, ChronoUnit.MILLIS);

        int successCount = refreshTokenMapper.insertRefreshToken(userLogin.getUserLoginId(), refreshToken, expiredAt);

        if(successCount < 1){
            throw new RefreshTokenException("토큰 저장 중 예상치 못한 에러가 발생했습니다", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //banner path 생성기
    private String generateBannerUrl(String userUuid){
        return FileConstants.BANNER + "/" + userUuid;
    }

    //회원가입
    @Transactional(rollbackFor = Exception.class)
    public void signup(SignupReqDto dto) throws IOException {
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

        //유저 정보 저장
        //배너 이미지 저장
        String file = dto.getBanner();
        String dir = generateBannerUrl(uuid);
        String newBannerUrl = localFileStorage.moveFromTmp(file, dir);
        user.setBanner(newBannerUrl);

        int successCount1 = userMapper.insertUserInfo(user);
        int successCount2 = userMapper.insertUserLogin(userLogin);
        if(successCount1 < 1 || successCount2 < 1){
            throw new SignupException("추가 중 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //로그인
    public SigninResDto signin(SigninReqDto reqDto){
        //로그인
        UserLogin userLogin = userMapper.selectUserLoginByUserid(reqDto.getUserid());
        if(userLogin == null){
            throw new SigninException("아이디가 존재하지 않습니다.", HttpStatus.BAD_REQUEST);
        }

        if(!encoder.matches(reqDto.getPasswd(), userLogin.getPasswd())){
            throw new SigninException("비밀번호가 틀립니다.", HttpStatus.BAD_REQUEST);
        }

        //유저 확인 후 토큰 발급
        User user = userMapper.getUserByUuid(userLogin.getUserUuid());
        //login 유저 table이랑 info 테이블이랑 다름 -> 거의 없음
        if(user == null){
            throw new SigninException("해당 유저가 존재하지 않습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        SigninResDto resDto = generateTokenPair(user);

        //저장
        saveRefreshToken(userLogin, resDto.getRefreshToken());

        return resDto;
    }

    //재발급
    @Transactional(rollbackFor = Exception.class)
    public SigninResDto refresh(
            String refreshTokenString
    ){
        //검증
        if(!jwtUtil.isRefreshToken(refreshTokenString)){
            throw new RefreshTokenException("refresh 토큰이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        //토큰 추출
        RefreshToken refreshToken = refreshTokenMapper.getRefreshTokenByToken(refreshTokenString);
        if(refreshToken == null){
            throw new RefreshTokenException("refresh 토큰이 없습니다.", HttpStatus.BAD_REQUEST);
        }

        //신규 accessToken 생성
        Claims claims;
        try{
            claims = jwtUtil.getClaims(refreshTokenString);
        }catch(ExpiredJwtException e){
            //만료
            refreshTokenMapper.deleteRefreshTokenById(refreshToken.getRefreshTokenId());
            throw new RefreshTokenException("Refresh 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED);
        }catch(JwtException e){
            //위조 - db에 저장된 claims가 위조된 상황
            refreshTokenMapper.deleteRefreshTokenById(refreshToken.getRefreshTokenId());
            throw new RefreshTokenException("Refresh 토큰이 위조되었습니다.", HttpStatus.FORBIDDEN);
        }

        //sub 추출, userLogin 추가 호출
        String userUuid = claims.get("sub", String.class);
        User user = userMapper.getUserByUuid(userUuid);
        UserLogin userLogin = userMapper.getUserLoginById(refreshToken.getUserLoginId());
        if(user == null || !user.getUserUuid().equals(userLogin.getUserUuid())){
            throw new RefreshTokenException("유저가 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }

        //새 토큰 발급
        SigninResDto resDto = generateTokenPair(user);

        //DB 업데이트
        refreshTokenMapper.deleteRefreshTokenById(refreshToken.getRefreshTokenId());
        saveRefreshToken(userLogin, resDto.getRefreshToken());

        return resDto;
    }
}

package com.archive.archive_project_backend.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntrypoint implements AuthenticationEntryPoint {

    public static final String EXPIRED_ERROR_MSG = """
            {
                "error" : "ACCESS_TOKEN_EXPIRED"
            }
            """;

    public static final String INVALID_ERROR_MSG = """
            {
                "error" : "ACCESS_TOKEN_INVALID"
            }
            """;

    public static final String UNAUTHORIZED_ERROR_MSG = """
            {
                "error" : "ACCESS_TOKEN_UNAUTHORIZED"
            }
            """;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        //언박싱
        Exception e = (Exception) request.getAttribute("exception");

        //응답헤더 설정
        response.setContentType("application/json; charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        //분기
        if(e instanceof ExpiredJwtException){
            response.getWriter().write(EXPIRED_ERROR_MSG);
        }else if(e instanceof JwtException){
            response.getWriter().write(INVALID_ERROR_MSG);
        }else{
            response.getWriter().write(UNAUTHORIZED_ERROR_MSG);
        }

        //끝
        response.getWriter().flush();
    }
}

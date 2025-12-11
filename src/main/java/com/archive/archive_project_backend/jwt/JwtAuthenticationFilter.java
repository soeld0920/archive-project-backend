package com.archive.archive_project_backend.jwt;

import com.archive.archive_project_backend.constants.JwtConstants;
import com.archive.archive_project_backend.exception.JwtAuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//전처리 및 후처리 용
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestMethod = request.getMethod();

        if(requestMethod.equalsIgnoreCase("OPTIONS")){
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(JwtConstants.HEADER);

        if(authHeader == null || !jwtUtil.isBearer(authHeader)){
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.removeBearer(authHeader);

        try{
            Claims claims = jwtUtil.getClaims(token);

            String type = claims.get("type", String.class);

            if(!type.equals(JwtConstants.ACCESS)){
                throw new JwtException("Not access token");
            }

            //추출
            String userUuid = claims.get("sub", String.class);
            String roleName = claims.get("role", String.class);

            GrantedAuthority jwtAuth = new SimpleGrantedAuthority(JwtConstants.ROLE + roleName);
            List<GrantedAuthority> authorities = List.of(jwtAuth);

            JwtAuthentication authentication = new JwtAuthentication(userUuid, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch(JwtException e){
            request.setAttribute("exception", e);

            throw new JwtAuthenticationException("Jwt 인증 실패");
        }

        filterChain.doFilter(request,response);
    }
}

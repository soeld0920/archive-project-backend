package com.archive.archive_project_backend.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

//인증 성공 시 생성됨
@RequiredArgsConstructor
public class JwtAuthentication implements Authentication {
    private final String userUuid; //식별자
    private final List<GrantedAuthority> authorities; //권한
    private boolean isAuthenticated = true; //인증여부

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return null; //세션 인증용
    }

    @Override
    public Object getDetails() {
        return null;//세션 정보용
    }

    @Override
    public String getPrincipal() {
        return userUuid;//식별자
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return userUuid;
    }
}

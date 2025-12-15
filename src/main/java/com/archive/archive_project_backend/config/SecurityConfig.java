package com.archive.archive_project_backend.config;

import com.archive.archive_project_backend.jwt.JwtAuthenticationEntrypoint;
import com.archive.archive_project_backend.jwt.JwtAuthenticationFilter;
import com.archive.archive_project_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtUtil jwtUtil;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){return new BCryptPasswordEncoder();}

    @Bean
    public JwtAuthenticationEntrypoint jwtAuthenticationEntrypoint(){return new JwtAuthenticationEntrypoint();};

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){return new JwtAuthenticationFilter(jwtUtil);};

    //cross origin resource sharing 에러 방지 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:5173"));
        config.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE","OPTIONS"));

        // ⭐ 핵심: preflight에서 요구하는 헤더 허용
        config.setAllowedHeaders(List.of("Content-Type", "Authorization", "X-Requested-With"));

        // ⭐ 쿠키(리프레시) 쓰면 필수
        config.setAllowCredentials(true);

        // (선택) 클라에서 읽어야 하는 헤더가 있으면
        config.setExposedHeaders(List.of("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
    //filterChain 설정
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.cors(Customizer.withDefaults());
        //csrf : 세션 기반 공격
        http.csrf(csrf -> csrf.disable());
        //폼 로그인
        http.formLogin(form -> form.disable());
        //세션기반 로그인
        http.httpBasic(basic -> basic.disable());
        //세션 로그아웃
        http.logout(lg -> lg.disable());
        //세션 무상태로 변경
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //필터 설정
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        //인증실패 처리
        http.exceptionHandling(e -> e.authenticationEntryPoint(jwtAuthenticationEntrypoint()));

        //url 권한 설정
        http.authorizeHttpRequests(auth -> {
            //auth.requestMatchers(HttpMethod.POST,"/api/writing/*/comment").authenticated();
            auth.anyRequest().permitAll();
        });

        return http.build();
    }
}

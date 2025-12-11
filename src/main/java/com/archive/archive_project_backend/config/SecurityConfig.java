package com.archive.archive_project_backend.config;

import com.archive.archive_project_backend.jwt.JwtAuthenticationEntrypoint;
import com.archive.archive_project_backend.jwt.JwtAuthenticationFilter;
import com.archive.archive_project_backend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration cors = new CorsConfiguration();

        cors.setAllowedOrigins(List.of("http;//localhost:3000"));

        cors.setAllowCredentials(true);
        cors.setExposedHeaders(List.of("Set-Cookie"));

        cors.addAllowedHeader(CorsConfiguration.ALL);
        cors.addAllowedMethod(CorsConfiguration.ALL);

        UrlBasedCorsConfigurationSource sc = new UrlBasedCorsConfigurationSource();

        sc.registerCorsConfiguration("/**", cors);

        return sc;
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
            auth.requestMatchers("/user/**").permitAll();
            auth.anyRequest().authenticated();
        });

        return http.build();
    }
}

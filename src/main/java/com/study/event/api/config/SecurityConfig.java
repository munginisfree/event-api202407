package com.study.event.api.config;

import com.study.event.api.auth.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

// 스프링 시큐리티 설정 파일
// 인터셉터, 필터 처리
// 세션 검증, 토큰 인증
// 권한처리
// OAuth2 - SNS 로그인
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // 비밀번호 암호화 객체 컨테이너 등록 (스프링에게 주입받는 설정)
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정 (스프링 부트 2.7버전 이전 인터페이스를 통해 오버라이딩)
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws  Exception {

        http
                .cors()
                .and()
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                // 세션 인증은 더 이상 사용하지 않음
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 아래의 URL 요청은 모두 허용
                .antMatchers("/", "/auth/**").permitAll()
                // 나머지 요청은 전부 인증(로그인) 후 진행해라
                .anyRequest().authenticated() // 인가 설정 on
        ;

        //토큰 위조 검사 커스텀 필터 필터체인에 연결
        // CorsFilter(spring 필터) 뒤에 커스텀 필터를 연결
        http.addFilterAfter(jwtAuthFilter, CorsFilter.class);

        return http.build();
    }

}

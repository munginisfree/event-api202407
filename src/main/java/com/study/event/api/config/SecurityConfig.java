package com.study.event.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

// 스프링 시큐리티 설정 파일
// 인터셉터, 필터 처리
// 세션 검증, 토큰 인증
// 권한처리
// OAuth2 - SNS 로그인
@EnableWebSecurity
public class SecurityConfig {

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
                .authorizeRequests()
                .antMatchers("/**").permitAll()
        ;

        return http.build();
    }

}

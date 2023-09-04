package io.dongyeop.springsecurityjwt.jwt;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * JwtFilter & TokenProvider를 SecurityConfig에 적용할 때 사용할 설정 클래스
 */
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    private TokenProvider tokenProvider;

    public JwtSecurityConfig(final TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    /**
     * JwtFilter를 Security 로직에 필터로 등록
     * @param http
     */
    @Override
    public void configure(final HttpSecurity http) {
        final JwtFilter jwtFilter = new JwtFilter(tokenProvider);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}

package io.dongyeop.springsecurityjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http
                .authorizeRequests()
                .requestMatchers("/api/hello").permitAll()
                .anyRequest().authenticated();

        return http.build();
    }
}

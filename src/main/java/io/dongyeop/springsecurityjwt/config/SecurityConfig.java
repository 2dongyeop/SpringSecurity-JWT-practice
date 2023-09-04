package io.dongyeop.springsecurityjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@Configuration
public class SecurityConfig {


    /**
     * Springboot 2.7 이전에 WebSecurityConfigurerAdapter를 상속받아
     * 오버라이딩하던 HttpSecurity 관련 설정이 이걸로 바뀜
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(new AntPathRequestMatcher("/api/hello")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated())

                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                        .disable()
                )
                .cors(cors -> cors.disable())

                // enable h2-console
                .headers(headers -> headers.frameOptions(options -> options.disable()))
        ;

        return http.build();
    }


    /**
     * Springboot 2.7 이전에 WebSecurity 관련 설정이 이걸로 바뀜
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/error"))
                .requestMatchers(new AntPathRequestMatcher("/favicon.ico"))
                .requestMatchers(toH2Console())
                ;
    }
}

package io.dongyeop.springsecurityjwt.config;

import io.dongyeop.springsecurityjwt.jwt.JwtAccessDeniedHandler;
import io.dongyeop.springsecurityjwt.jwt.JwtAuthenticationEntryPoint;
import io.dongyeop.springsecurityjwt.jwt.JwtSecurityConfig;
import io.dongyeop.springsecurityjwt.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    public SecurityConfig(
            final TokenProvider tokenProvider,
            final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            final JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Springboot 2.7 이전에 WebSecurityConfigurerAdapter를 상속받아
     * 오버라이딩하던 HttpSecurity 관련 설정이 이걸로 바뀜
     */
    @Bean
    public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers(new AntPathRequestMatcher("/api/hello")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/authenticate")).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/signup")).permitAll()
                        .requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
                        .anyRequest().authenticated())

                /* token을 사용하기 때문 */
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
                        .disable())

                .cors(cors -> cors.disable())

                .exceptionHandling(handling -> handling
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                        .accessDeniedHandler(jwtAccessDeniedHandler))

                // enable h2-console
                .headers(headers -> headers.frameOptions(options -> options.disable()))

                /* session을 사용하지 않음 */
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .apply(new JwtSecurityConfig(tokenProvider))
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

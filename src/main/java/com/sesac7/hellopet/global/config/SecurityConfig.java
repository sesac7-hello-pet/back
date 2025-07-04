package com.sesac7.hellopet.global.config;

import com.sesac7.hellopet.global.exception.handler.RestAccessDeniedHandler;
import com.sesac7.hellopet.global.exception.handler.RestAuthEntryPoint;
import com.sesac7.hellopet.global.filter.JwtFilter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${app.front-url}")
    private String frontOrigin;
    private final JwtFilter jwtFilter;
    private final RestAccessDeniedHandler deniedHandler;
    private final RestAuthEntryPoint entryPoint;

    private final String[] authMatcher = {"/auth/**"};
    private final String[] userMatcher = {"/users/**"};
    private final String[] meMatcher = {"/me/**"};
    private final String[] adminMatcher = {"/admin/**"};
    private final String[] boardMatcher = {"/boards", "/boards/**"};
    private final String[] applicationMatcher = {"/applications", "/applications/**"};
    private final String[] announcementMatcher = {"/announcements", "/announcements/**",
            "/announcements/*/applications/**"};

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // CORS 설정
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // CSRF, 인가 등 기존 설정
                .csrf(csrf -> csrf.disable())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(authMatcher[0], userMatcher[0]).permitAll()
                        .requestMatchers(meMatcher[0]).authenticated()
                        .requestMatchers(adminMatcher[0]).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, boardMatcher[1], announcementMatcher[1]).permitAll()
                        .requestMatchers(HttpMethod.GET, announcementMatcher[2], applicationMatcher[1])
                        .hasRole("SHELTER")

                        .requestMatchers(HttpMethod.POST, boardMatcher[0], applicationMatcher[0]).authenticated()
                        .requestMatchers(HttpMethod.POST, applicationMatcher[0]).hasRole("USER")
                        .requestMatchers(HttpMethod.POST, announcementMatcher[0]).hasRole("SHELTER")

                        .requestMatchers(HttpMethod.PUT, boardMatcher[1]).authenticated()
                        .requestMatchers(HttpMethod.PUT, announcementMatcher[1]).hasRole("SHELTER")
                        .requestMatchers(HttpMethod.PUT, announcementMatcher[2]).hasRole("SHELTER")

                        .requestMatchers(HttpMethod.DELETE, boardMatcher[1]).authenticated()
                        .requestMatchers(HttpMethod.DELETE, applicationMatcher[1]).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE, announcementMatcher[1]).hasRole("SHELTER")

                        .anyRequest().authenticated())
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(deniedHandler));
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of(frontOrigin));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}

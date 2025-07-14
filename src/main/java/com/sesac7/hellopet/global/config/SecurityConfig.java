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

    private final String[] authMatcher = {"/auth/**", "/auth/logout", "/auth/check-password"};
    private final String[] userMatcher = {"/users/**"};
    private final String[] meMatcher = {"/me/**"};
    private final String[] adminMatcher = {"/admin/**"};
    private final String[] boardMatcher = {"/boards", "/boards/**"};
    private final String[] applicationMatcher = {"/applications", "/applications/**"};
    private final String[] announcementMatcher = {"/announcements", "/announcements/**",
            "/announcements/*/applications/**"};

    /**
     * custom filter체인을 사용하기 위한 메서드입니다.
     * 여기서 다양한 필터를 설정 할 수 있습니다.
     *
     * @param http
     * @return
     * @throws Exception
     */
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
                        .requestMatchers(authMatcher[1], authMatcher[2]).authenticated()
                        .requestMatchers(authMatcher[0], userMatcher[0]).permitAll()
                        .requestMatchers(meMatcher[0]).authenticated()
                        .requestMatchers(adminMatcher[0]).hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, announcementMatcher[2], applicationMatcher[1])
                        .hasRole("SHELTER")
                        .requestMatchers(HttpMethod.GET, boardMatcher[1], announcementMatcher[1]).permitAll()

                        .requestMatchers(HttpMethod.POST, boardMatcher[0]).authenticated()
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
                        .accessDeniedHandler(deniedHandler))
                .logout(logout -> logout.logoutUrl(authMatcher[1])
                                        .logoutSuccessHandler((req, res, auth) -> res.setStatus(204)));
        ;
        return http.build();
    }

    /**
     * cors 설정을 위한 메서드입니다.
     * 스프링에서는 아무데서나 데이터를 받을 수 없게
     * 허용된 path에서만 받을 수 있는 cors 옵션이란게 있는데
     * 여기에 프로젝트의 프론트 url을 입력하여
     * 프론트에서 오는 요청을 허용하는 설정입니다.
     *
     * @return
     */
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

    /**
     * Authentication 객체를 사용할 때
     * userDetails의 password는 반드시 암호화 되어있어야 하기 때문에
     * 암호화를 위한 password 인코더를 설정해주는 매서드입니다.
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Authentication 객체를 활용하거나 Daoprovider를 사용하는
     * AuthenticationManager를 선언해주는 메서드입니다.
     *
     * @param cfg
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}

package com.sesac7.hellopet.global.filter;

import com.sesac7.hellopet.common.utils.CustomUserDetails;
import com.sesac7.hellopet.common.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final AntPathMatcher matcher = new AntPathMatcher();
    private static final String[] WHITELIST = {
            "/users/**",
            "/auth/**"
    };

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String access = jwtUtil.findCookie(request, "accessToken");
        String refresh = jwtUtil.findCookie(request, "refreshToken");

        if (access != null && !jwtUtil.isTokenExpired(access)) {
            String email = jwtUtil.getEmailFromToken(access);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                CustomUserDetails ud = (CustomUserDetails) userDetailsService.loadUserByUsername(email);

                if (jwtUtil.validateToken(access, ud)) {
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(ud, null, ud.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }

            filterChain.doFilter(request, response);
            return;
        }

        if ((access == null || jwtUtil.isTokenExpired(access)) && (refresh != null)) {
            throw new CredentialsExpiredException("ACCESS TOKEN이 만료되었습니다.");
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();         // /api/v1/users/signup
        return Arrays.stream(WHITELIST)
                     .anyMatch(p -> matcher.match(p, uri));
    }
}

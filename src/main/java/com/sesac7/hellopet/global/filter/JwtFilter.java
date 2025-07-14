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
            "/api/v1/users/**",
            "/api/v1/auth/login",
            "/api/v1/auth/signup",
            "/api/v1/auth/refresh"
    };

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    /**
     * jwt필터가 어떻게 동작할지 설정하는 메서드입니다.
     * 현재 엑세스 토큰과 리프래시 토큰을 찾아서
     * 엑세스 토큰이 있으면 토큰에서 이메일을 찾고
     * 이메일로 유저를 찾고
     * 찾은 유저를 userDetails로 변환하고
     * userDetails로 authToken을 만들고
     * authToken에 request에서 받은 메타데이터를 설정하고
     * authToken을 SecurityContextHolder에 넣어주는 과정입니다.
     * 만일, 엑세스 토큰이나 리프래시 토큰이 잘못되어있다면 다음 필터로 넘어가거나 예외를 발생시킵니다.
     *
     * @param request
     * @param response
     * @param filterChain
     * @throws ServletException
     * @throws IOException
     */
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

    /**
     * 필터를 거치지 않을 목록입니다.
     * 만일 리프래시 토큰만 가지고 엑세스 토큰을 재발급 받으려고 /refresh 로
     * 요청을 보낸다면
     * 리프래시 토큰이 없기 때문에 위의 doFilter에서 걸러지게 되어 컨트롤러로 전달 될 수 없기 때문에
     * whiteList를 만들어 위의 doFilter를 건너게 해줬습니다.
     *
     * @param request current HTTP request
     * @return
     * @throws ServletException
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();         // /api/v1/users/signup
        return Arrays.stream(WHITELIST)
                     .anyMatch(p -> matcher.match(p, uri));
    }
}

package com.spring.board.config;

import com.spring.board.service.JwtService;
import com.spring.board.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtService jwtService, UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String BEARER_PREFIX = "Bearer ";
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        SecurityContext securityContext = SecurityContextHolder.getContext();

        if(!ObjectUtils.isEmpty(authorization)
                && authorization.startsWith(BEARER_PREFIX)
                && securityContext.getAuthentication() == null
        ) {

            String accessToken = authorization.substring(BEARER_PREFIX.length()); // accessToken 추출
            String username= jwtService.getUsername(accessToken); // accessToken으로부터 username 추출
            UserDetails userDetails = userService.loadUserByUsername(username); // username으로부터 UserDetails 추출

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()); // UsernamePasswordAuthenticationToken 생성,
            // UsernamePasswordAuthenticationToken의 인자는 UserDetails, credentials, authorities 순서
            // 각각은 UserDetails의 구현체, credentials는 패스워드, authorities는 권한 목록
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); // WebAuthenticationDetailsSource를 이용하여 WebAuthenticationDetails 생성
            securityContext.setAuthentication(authenticationToken); // SecurityContext에 authenticationToken을 설정
            SecurityContextHolder.setContext(securityContext); // SecurityContextHolder에 SecurityContext 설정
        }

        filterChain.doFilter(request, response);
    }
}

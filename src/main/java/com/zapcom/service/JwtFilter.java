package com.zapcom.service;

import com.zapcom.service.impl.UserDetailsService;
import com.zapcom.constants.JwtMessageConstants;
import com.zapcom.exceptions.AuthenticationException;
import com.zapcom.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtilService jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (path.equals("/auth/login")
                || path.equals("/auth/register")
                || path.equals("/favicon.ico")
                || path.startsWith("/auth/verify/")
                || path.equals("/auth/customer/getToken")
                || path.equals("/auth/customer/get_api_key")
                || path.equals("/auth/customer/getCustomer")) {
            log.info("verify");
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Request path: " + path);
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationException(JwtMessageConstants.MISSING_AUTHENTICATION_HEADER);
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new AuthenticationException(JwtMessageConstants.INVALID_JWT);
        }

        if (tokenRepository.isAccessTokenBlacklisted(token)) {
            throw new AuthenticationException(JwtMessageConstants.TOKEN_BLACKLISTED);
        }

        String email = jwtUtil.extractEmail(token);
        String storedToken = tokenRepository.getAccessTokenKey(email);
        if (!token.equals(storedToken)) {
            throw new AuthenticationException(JwtMessageConstants.TOKEN_EXPIRED_OR_INVALID);
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);

    }
}


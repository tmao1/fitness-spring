package com.chyld.filters;

import com.chyld.dtos.AuthDto;
import com.chyld.utilities.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.chyld.entities.*;

public class JwtLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtLoginFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil, UserDetailsService userDetailsService, AuthenticationManager authManager) {
        super(defaultFilterProcessesUrl);

        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        setAuthenticationManager(authManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        final AuthDto auth = new ObjectMapper().readValue(request.getInputStream(), AuthDto.class);
        final UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword());
        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        User authenticatedUser = (User) userDetailsService.loadUserByUsername(authResult.getName());
        String token = jwtUtil.generateToken(authenticatedUser);
        response.setHeader("Authorization", "Bearer " + token);
        SecurityContextHolder.getContext().setAuthentication(jwtUtil.tokenFromStringJwt(token));
    }
}

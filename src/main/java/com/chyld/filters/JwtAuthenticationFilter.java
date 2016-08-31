package com.chyld.filters;

import com.chyld.security.JwtToken;
import com.chyld.utilities.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

@Component

public class JwtAuthenticationFilter extends GenericFilterBean {
    private final JwtUtil jwtUtil;

    @Autowired
    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest request = (HttpServletRequest) req;
        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }

        String authToken = URLDecoder.decode(header, "UTF-8").substring(7);
        JwtToken token = jwtUtil.tokenFromStringJwt(authToken);
        SecurityContextHolder.getContext().setAuthentication(token);
        chain.doFilter(req, response);
    }
}

package com.goquizit.utils;

import com.goquizit.services.UserService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private UserService userService;

    public AuthorizationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(null);

        String token = request.getHeader(AuthenticationFilter.HEADER);
        if (token == null || !token.startsWith(AuthenticationFilter.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String username = getUsernameFromToken(token);
        if (username == null) {
            chain.doFilter(request, response);
            return;
        }

        UserDetails userDetails = userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        chain.doFilter(request, response);
    }

    private String getUsernameFromToken(String tokenWithPrefix) {
        String token = tokenWithPrefix.replace(AuthenticationFilter.TOKEN_PREFIX, "");
        return Jwts.parser().setSigningKey(AuthenticationFilter.SECRET).parseClaimsJws(token).getBody().getSubject();
    }
}

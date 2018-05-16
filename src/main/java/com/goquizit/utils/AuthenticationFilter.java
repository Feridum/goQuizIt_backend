package com.goquizit.utils;

import com.goquizit.model.User;
import org.apache.logging.log4j.core.util.IOUtils;
import org.json.JSONObject;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

import com.google.gson.Gson;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    static final String SECRET = "Secret";
    static final String HEADER = "Authorization";
    static final String TOKEN_PREFIX = "Bearer ";

    @java.beans.ConstructorProperties({"authenticationManager"})
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if ("application/json".equals(request.getHeader("Content-Type"))) {
            LoginRequest loginRequest = this.getLoginRequest(request);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());
            return authenticationManager.authenticate(token);
        } else {
            return super.attemptAuthentication(request, response);
        }
    }

    private LoginRequest getLoginRequest(HttpServletRequest request) {
        BufferedReader reader = null;
        LoginRequest loginRequest = null;
        try {
            reader = request.getReader();
            Gson gson = new Gson();
            loginRequest = gson.fromJson(reader, LoginRequest.class);
        } catch (IOException ex) {
            logger.error(null, ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                logger.error(null, ex);
            }
        }

        if (loginRequest == null) {
            loginRequest = new LoginRequest();
        }
        return loginRequest;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        User user = (User) authResult.getPrincipal();
        String token = Jwts.builder()
                .setSubject(user.getUsername())
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();

        response.setContentType("application/json");
        JSONObject res = new JSONObject();
        res.put("access_token", token);
        res.put("token_type", TOKEN_PREFIX);
        response.getWriter().print(res);
    }
}

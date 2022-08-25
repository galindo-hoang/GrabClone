package com.example.authentication.security.filter;

import com.auth0.jwt.algorithms.Algorithm;
import com.example.authentication.model.entity.QRole;
import com.example.authentication.model.entity.QUser;
import com.example.authentication.model.payload.JwtResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final EntityManager entityManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, EntityManager entityManager) {
        this.authenticationManager = authenticationManager;
        this.entityManager = entityManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userName, password);
        return authenticationManager.authenticate(token);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        User user = (User) authResult.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String accessToken = com.auth0.jwt.JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 28800 * 1000)) //8h
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
        String refreshToken = com.auth0.jwt.JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 86400 * 1000))//24h
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);
        List<com.example.authentication.model.entity.User> userDb =
                new JPAQuery<com.example.authentication.model.entity.User>(entityManager)
                        .from(QUser.user)
                        .leftJoin(QUser.user.roles, QRole.role).fetchJoin()
                        .fetch();
        com.example.authentication.model.entity.User userDbFind = userDb.stream().filter(u -> u.getUsername().equals(user.getUsername())).findFirst().get();

        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken, user,userDbFind.getPhonenumber());
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), jwtResponse);
    }
}

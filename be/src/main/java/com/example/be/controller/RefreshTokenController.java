package com.example.be.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.be.exception.InvalidTokenException;
import com.example.be.exception.UsernameNotFoundException;
import com.example.be.model.entity.QRole;
import com.example.be.model.entity.QUser;
import com.example.be.model.entity.User;
import com.example.be.service.twilio.OtpService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@RestController
public class RefreshTokenController {
    @Autowired
    private EntityManager em;
    @Autowired
    private OtpService otpService;

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest httpServletRequest,
                             HttpServletResponse httpServletResponse) throws IOException {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT jwt = verifier.verify(refreshToken);
                String username = jwt.getSubject();
                User user = new JPAQuery<User>(em)
                        .from(QUser.user)
                        .leftJoin(QUser.user.roles, QRole.role).fetchJoin()
                        .where(QUser.user.username.eq(username))
                        .fetchFirst();
                if (user == null) {
                    throw new UsernameNotFoundException("User Not Found with username: " + username);
                }
                List<String> roles = new ArrayList<>();
                user.getRoles()
                        .forEach(role -> roles.add(role.getName().toString()));
                String accessToken = com.auth0.jwt.JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                        .withIssuer(httpServletRequest.getRequestURL().toString())
                        .withClaim("roles", roles)
                        .sign(algorithm);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("accessToken", accessToken);
                tokens.put("refreshToken", refreshToken);
                httpServletResponse.setContentType("application/json");
                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), tokens);
            } catch (Exception e) {
                httpServletResponse.setHeader("Error", e.getMessage());
                httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("errorMessage", e.getMessage());
                httpServletResponse.setContentType("application/json");
                new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), tokens);
            }
        } else {
            throw new InvalidTokenException("Invalid token");
        }
    }
}

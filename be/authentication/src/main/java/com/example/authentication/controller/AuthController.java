package com.example.authentication.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.authentication.exception.InvalidTokenException;
import com.example.authentication.exception.UsernameNotFoundException;
import com.example.authentication.model.dto.UserDto;
import com.example.authentication.model.entity.Role;
import com.example.authentication.model.entity.User;
import com.example.authentication.service.RoleService;
import com.example.authentication.service.UserService;
import com.example.authentication.utils.ModelMapperGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;

@RestController
@Slf4j
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private RoleService roleService;
    @Autowired
    public AuthController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        ModelMapperGenerator.getUserTypeMap(modelMapper);
    }
    @PostMapping("/user/register")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto user) {
        //Get current resource url
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/register").toUriString());
        User userSaving = modelMapper.map(user, User.class);
        userSaving.setPassword(user.getPassword());
        userSaving.setRoles(List.of(roleService.findByName(Role.RoleName.ROLE_USER)));
        return ResponseEntity.created(uri).body(
                modelMapper.map(userService.saveUser(userSaving)
                        , UserDto.class));
    }
    @PostMapping("/driver/register")
    public ResponseEntity<UserDto> saveDriver(@RequestBody UserDto user) {
        //Get current resource url
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/driver/register").toUriString());
        User userSaving = modelMapper.map(user, User.class);
        userSaving.setPassword(user.getPassword());
        userSaving.setRoles(List.of(roleService.findByName(Role.RoleName.ROLE_DRIVER)));
        return ResponseEntity.created(uri).body(
                modelMapper.map(userService.saveUser(userSaving)
                        , UserDto.class));
    }
    @GetMapping("/test")
    public ResponseEntity<String> ss(){
        log.info("test");
        return ResponseEntity.ok("ok");
    }

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
                List<User> userList = userService.findByUsername(username);
                User user=userList.get(0);
                if (user == null) {
                    throw new UsernameNotFoundException("User Not Found with username: " + username);
                }
                List<String> roles = new ArrayList<>();
                user.getRoles()
                        .forEach(role -> roles.add(role.getName().toString()));
                String accessToken = com.auth0.jwt.JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 28800 * 1000))
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

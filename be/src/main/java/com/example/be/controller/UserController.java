package com.example.be.controller;

import com.example.be.model.dto.UserDto;
import com.example.be.model.entity.Role;
import com.example.be.model.entity.User;
import com.example.be.service.UserService;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired

    private UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        TypeMap<User, UserDto> userToUserDtoTypeMap = modelMapper.createTypeMap(User.class, UserDto.class);
        //skip role
        userToUserDtoTypeMap.addMappings(mapper -> mapper.skip(UserDto::setRoles));
        userToUserDtoTypeMap.addMappings(mapper -> mapper.skip(UserDto::setPassword));
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers() {
        return ResponseEntity.ok(
                userService.findAll().stream()
                        .map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList()));
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> saveUser(@RequestBody UserDto user) {
        //Get current resource url
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/save").toUriString());
        return ResponseEntity.created(uri).body(
                modelMapper.map(userService.saveUser(modelMapper.map(user, User.class))
                        , UserDto.class));
    }
}

@Data
class RoleToUserForm {
    private User userName;
    private Role roleName;
}
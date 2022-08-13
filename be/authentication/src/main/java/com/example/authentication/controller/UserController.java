package com.example.authentication.controller;

import com.example.authentication.model.dto.UserDto;
import com.example.authentication.model.entity.Role;
import com.example.authentication.model.entity.User;
import com.example.authentication.service.UserService;
import com.example.authentication.utils.ModelMapperGenerator;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired

    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public UserController(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        ModelMapperGenerator.getUserTypeMap(modelMapper);
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

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<UserDto> getUser(@PathVariable String phoneNumber) {
        return ResponseEntity.ok(modelMapper.map(userService.findByUserByPhoneNumber(phoneNumber), UserDto.class));
    }

}

@Data
class RoleToUserForm {
    private User userName;
    private Role roleName;
}
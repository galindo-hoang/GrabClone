package com.example.be.controller;

import com.example.be.model.dto.RoleDto;
import com.example.be.model.dto.UserDto;
import com.example.be.model.entity.Role;
import com.example.be.service.RoleService;
import com.example.be.service.UserService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private ModelMapper modelMapper;
    @PostMapping("/save")
    public ResponseEntity<RoleDto> saveUser(@RequestBody RoleDto roleDto) {
        //Get current resource url
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/roles/save").toUriString());
        return ResponseEntity.created(uri).body(
                modelMapper.map(roleService.saveRole(modelMapper.map(roleDto, Role.class))
                        , RoleDto.class));
    }
}
package com.example.authentication.controller;

import com.example.authentication.model.dto.RoleDto;
import com.example.authentication.model.entity.Role;
import com.example.authentication.service.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
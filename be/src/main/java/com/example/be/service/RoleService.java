package com.example.be.service;

import com.example.be.model.dto.RoleDto;
import com.example.be.model.entity.Role;


public interface RoleService {
    Role findOne(Integer id);
    Role saveRole(Role role);
}

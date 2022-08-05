package com.example.authentication.service;

import com.example.authentication.model.entity.Role;


public interface RoleService {
    Role findOne(Integer id);

    Role saveRole(Role role);
}

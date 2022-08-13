package com.example.authentication.service.impl;

import com.example.authentication.model.entity.QRole;
import com.example.authentication.model.entity.Role;
import com.example.authentication.repository.RoleRepository;
import com.example.authentication.service.RoleService;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleRepository roleRepository;

    @PersistenceContext
    private EntityManager em;
    @Override
    public Role findOne(Integer id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role saveRole(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role findByName(Role.RoleName name) {
        return roleRepository.findByName(name);
    }


}

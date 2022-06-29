package com.example.be.service;

import com.example.be.model.entity.User;

import java.util.List;

public interface UserService {
    User findOne(Integer id);
    List<User> findAll();
}

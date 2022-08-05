package com.example.authentication.service;

import com.example.authentication.model.entity.User;

import java.util.List;

public interface UserService {
    User findOne(Integer id);

    List<User> findAll();

    List<User> findByUsername(String username);

    User saveUser(User user);

    User findByUserByPhoneNumber(String phonenumber);
}

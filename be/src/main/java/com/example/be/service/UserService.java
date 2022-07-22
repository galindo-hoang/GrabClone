package com.example.be.service;

import com.example.be.model.dto.UserDto;
import com.example.be.model.entity.Role;
import com.example.be.model.entity.User;

import java.util.List;

public interface UserService {
    User findOne(Integer id);
    List<User> findAll();
    User findByUsername(String username);
    User saveUser(User user);
    User findByUserByPhoneNumber(String phonenumber);
}

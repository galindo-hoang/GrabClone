package com.example.be.service.impl;

import com.example.be.model.entity.User;
import com.example.be.repository.UserRepository;
import com.example.be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Override
    public User findOne(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }
}

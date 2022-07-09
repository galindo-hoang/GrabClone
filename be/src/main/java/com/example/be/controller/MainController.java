package com.example.be.controller;
import com.example.be.model.dto.UserDto;
import com.example.be.model.entity.User;
import com.example.be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/users")
public class MainController {
    @Autowired
    private UserService userService;
    @GetMapping
    public List<User> getStudent() {
        userService.findAll().stream().forEach(System.out::println);
        return userService.findAll();
    }
}

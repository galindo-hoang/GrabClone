package com.example.be;


import com.example.be.model.entity.Role;
import com.example.be.model.entity.User;
import com.example.be.service.RoleService;
import com.example.be.service.UserService;
import com.example.be.service.NotificationService;
import com.example.be.service.twilio.OtpConfig;
import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication
public class BeApplication {
    public static void main(String[] args) {
        SpringApplication.run(BeApplication.class, args);
    }

    //create a bean for the password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //init twilio after Server Application is initialized
    @Autowired
    private OtpConfig otpConfig;
    @PostConstruct
    public void initTwilio() {
        Twilio.init(otpConfig.getAccountSid(), otpConfig.getAuthToken());
    }

//    @Bean
//    CommandLineRunner runner(UserService userService, RoleService roleService) {
//        return args -> {
//            Role roleAdmin = roleService.saveRole(new Role(Role.RoleName.ROLE_ADMIN));
//            Role roleUser = roleService.saveRole(new Role(Role.RoleName.ROLE_USER));
//            Role roleDriver = roleService.saveRole(new Role(Role.RoleName.ROLE_DRIVER));
//            Role roleTelephonist = roleService.saveRole(new Role(Role.RoleName.ROLE_TELEPHONIST));
//
//            User userQuan = new User("quan", "123","0833759401");
//            User userPhuc = new User("phuc", "123","0833759409");
//            User userHuy = new User("huy", "123","0833759402");
//            User userThieu = new User( "thieu", "123","0833759405");
//
//            userQuan.setRoles(List.of(roleAdmin, roleUser));
//            userService.saveUser(userQuan);
//            userPhuc.setRoles(List.of(roleDriver));
//            userService.saveUser(userPhuc);
//            userHuy.setRoles(List.of(roleTelephonist));
//            userService.saveUser(userHuy);
//            userThieu.setRoles(List.of(roleUser));
//            userService.saveUser(userThieu);
//
//        };
//    }
}

package com.example.authentication;

import com.example.authentication.model.entity.Role;
import com.example.authentication.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(scanBasePackages = {
        "com.example.authentication"
})
@EnableEurekaClient
public class AuthenticationApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthenticationApplication.class, args);
    }

    //create a bean for the password encoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    CommandLineRunner runner(RoleService roleService) {
//        return args -> {
//            roleService.saveRole(new Role(Role.RoleName.ROLE_ADMIN));
//            roleService.saveRole(new Role(Role.RoleName.ROLE_USER));
//            roleService.saveRole(new Role(Role.RoleName.ROLE_DRIVER));
//            roleService.saveRole(new Role(Role.RoleName.ROLE_TELEPHONIST));
//
//        };
//    }
}

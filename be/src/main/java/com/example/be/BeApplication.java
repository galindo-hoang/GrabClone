package com.example.be;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BeApplication.class, args);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
//    @Bean
//    CommandLineRunner runner(UserService userService, RoleService roleService) {
//        return args -> {
//            Role roleAdmin = roleService.saveRole(new Role(Role.RoleName.ROLE_ADMIN));
//            Role roleUser = roleService.saveRole(new Role(Role.RoleName.ROLE_USER));
//            Role roleDriver = roleService.saveRole(new Role(Role.RoleName.ROLE_DRIVER));
//            Role roleTelephonist = roleService.saveRole(new Role(Role.RoleName.ROLE_TELEPHONIST));
//
//            User userQuan = new User("quan", "quan", "123");
//            User userPhuc = new User("phuc", "phuc", "123");
//            User userHuy = new User("huy", "huy", "123");
//            User userThieu = new User("thieu", "thieu", "123");
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

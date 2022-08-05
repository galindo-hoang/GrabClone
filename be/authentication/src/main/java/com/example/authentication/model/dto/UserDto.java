package com.example.authentication.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String username;
    private String password;
    private String phonenumber;
    private List<RoleDto> roles = new ArrayList<>();
    private Integer version;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", version=" + version +
                '}';
    }


}

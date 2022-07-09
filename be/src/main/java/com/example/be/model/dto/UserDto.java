package com.example.be.model.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String name;
    private String username;
    private String password;
    private List<RoleDto> roles=new ArrayList<>();
    private Integer version;

    @Override
    public String toString() {
        return "UserDto{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userName='" + username + '\'' +
                ", password='" + password + '\'' +
                ", version=" + version +
                '}';
    }


}

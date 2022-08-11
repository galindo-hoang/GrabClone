package com.example.authentication.utils;

import com.example.authentication.model.dto.UserDto;
import com.example.authentication.model.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;

public class ModelMapperGenerator {

   public static TypeMap<User, UserDto> getUserTypeMap(ModelMapper modelMapper) {
       TypeMap<User, UserDto> typeMap = modelMapper.getTypeMap(User.class, UserDto.class);
       if (typeMap==null){
           TypeMap<User, UserDto> userToUserDtoTypeMap = modelMapper.createTypeMap(User.class, UserDto.class);
           //skip role
           userToUserDtoTypeMap.addMappings(mapper -> mapper.skip(UserDto::setRoles));
           userToUserDtoTypeMap.addMappings(mapper -> mapper.skip(UserDto::setPassword));
           return userToUserDtoTypeMap;
       }
       return typeMap;
   }
}

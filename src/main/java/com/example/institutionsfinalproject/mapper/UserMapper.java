package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.UserEntity;
import com.example.institutionsfinalproject.entity.dto.UserDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDTO toDto (UserEntity userEntity);
    UserEntity toEntity (UserDTO userDTO);
}

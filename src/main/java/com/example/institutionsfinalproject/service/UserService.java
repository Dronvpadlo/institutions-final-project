package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.Role;
import com.example.institutionsfinalproject.entity.UserEntity;
import com.example.institutionsfinalproject.entity.dto.UserDTO;
import com.example.institutionsfinalproject.entity.dto.UserRegistrationDTO;
import com.example.institutionsfinalproject.mapper.UserMapper;
import com.example.institutionsfinalproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public UserDTO createUser(UserRegistrationDTO userRegistrationDTO){
        UserEntity userEntity = userMapper.toEntity(userRegistrationDTO);
        userEntity.setRole(Role.USER);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    public List<UserDTO> getAllUsers(){
        List<UserEntity> users = userRepository.findAll();
        return users.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }
}

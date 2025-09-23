package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.Role;
import com.example.institutionsfinalproject.entity.UserEntity;
import com.example.institutionsfinalproject.entity.dto.UserDTO;
import com.example.institutionsfinalproject.entity.dto.UserRegistrationDTO;
import com.example.institutionsfinalproject.mapper.UserMapper;
import com.example.institutionsfinalproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public void deleteUser(String id){
        userRepository.deleteById(id);
    }

    public Optional<UserDTO> getUserById(String id){
        return userRepository.findById(id)
                .map(userMapper::toDto);
    }

    public Optional<UserDTO> putUser(String id, UserDTO userDTO){
        return userRepository.findById(id)
                .map(existedUser -> {
                    existedUser.setName(userDTO.getName());
                    existedUser.setSurname(userDTO.getSurname());
                    existedUser.setAddress(userDTO.getAddress());
                    existedUser.setAge(userDTO.getAge());
                    existedUser.setEmail(userDTO.getEmail());
                    userRepository.save(existedUser);
                    return userMapper.toDto(existedUser);
                });
    }
    public Optional<UserDTO> patchUser(String id, Map<String, Object> updates){
        return userRepository.findById(id)
                .map(user -> {
                    updates.forEach((key, value) -> {
                        if (!"role".equals(key)) {
                            switch (key) {
                                case "name":
                                    user.setName((String) value);
                                    break;
                                case "surname":
                                    user.setSurname((String) value);
                                    break;
                                case "address":
                                    user.setAddress((String) value);
                                    break;
                                case "email":
                                    user.setEmail((String) value);
                                    break;
                                case "age":
                                    if (value instanceof Number) {
                                        user.setAge(((Number) value).longValue());
                                    }
                                    break;
                            }
                        }
                    });
                    UserEntity updatedUser = userRepository.save(user);
                    return userMapper.toDto(updatedUser);
                });
    }


    public Optional<UserDTO> setRole(String id, Role newRole){
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(newRole);
                    userRepository.save(user);
                    return userMapper.toDto(user);
                });
    }
}

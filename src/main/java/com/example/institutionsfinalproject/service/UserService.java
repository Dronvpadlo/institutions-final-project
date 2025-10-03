package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.Role;
import com.example.institutionsfinalproject.entity.UserEntity;
import com.example.institutionsfinalproject.entity.dto.ResponseDTO;
import com.example.institutionsfinalproject.entity.dto.UserDTO;
import com.example.institutionsfinalproject.entity.dto.UserRegistrationDTO;
import com.example.institutionsfinalproject.mapper.UserMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import com.example.institutionsfinalproject.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final InstitutionRepository institutionRepository;

    private final InstitutionService institutionService;

    public UserService(UserRepository userRepository, UserMapper userMapper, InstitutionRepository institutionRepository, InstitutionService institutionService){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.institutionRepository = institutionRepository;
        this.institutionService = institutionService;
    }

    public UserDTO createUser(UserRegistrationDTO userRegistrationDTO){
        UserEntity userEntity = userMapper.toEntity(userRegistrationDTO);
        userEntity.setRole(Role.USER);
        UserEntity savedUser = userRepository.save(userEntity);
        return userMapper.toDto(savedUser);
    }

    public ResponseDTO<UserDTO> getAllUsers(int skip, int limit){
        Pageable pageable = PageRequest.of(skip/limit, limit);
        Page<UserEntity> userPage = userRepository.findAll(pageable);

        List<UserDTO> users = userPage.getContent()
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
        return new ResponseDTO<>(users, userPage.getTotalElements(), skip, limit);
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

    public Optional<UserDTO> setFavoriteInstitution(String userId, String institutionId){
    return userRepository.findById(userId)
                .map(user ->{
                    institutionRepository.findById(institutionId).ifPresent(institution -> {
                        List<String> userFavoriteInstitutionIds = user.getFavoriteInstitutionsIds();
                        if (userFavoriteInstitutionIds == null){
                            userFavoriteInstitutionIds = new ArrayList<>();
                        }
                        if (!userFavoriteInstitutionIds.contains(institutionId)){
                            userFavoriteInstitutionIds.add(institution.getId());
                            user.setFavoriteInstitutionsIds(userFavoriteInstitutionIds);
                            userRepository.save(user);
                            institutionService.incrementLikes(institutionId);
                        }
                    });
                return userMapper.toDto(user);
                });

    }

    public Optional<UserDTO> removeFavoriteInstitution(String userId, String institutionId){
        return userRepository.findById(userId)
                .map(user ->{
                    if (institutionRepository.existsById(institutionId)) {
                        List<String> userFavoriteInstitutionIds = user.getFavoriteInstitutionsIds();

                        if (userFavoriteInstitutionIds != null){
                            userFavoriteInstitutionIds.remove(institutionId);
                            user.setFavoriteInstitutionsIds(userFavoriteInstitutionIds);
                            userRepository.save(user);
                            institutionService.decrementLikes(institutionId);
                        }
                    }
                    return userMapper.toDto(user);
                });

    }
}

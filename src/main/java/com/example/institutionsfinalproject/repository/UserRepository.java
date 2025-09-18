package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
}

package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.UserEntity;
import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {

    @NonNull
    Page<UserEntity> findAll(@NonNull Pageable pageable);
}

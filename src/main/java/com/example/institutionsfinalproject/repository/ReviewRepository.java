package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.ReviewEntity;
import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
    @NonNull
    Page<ReviewEntity> findAll(@NonNull Pageable pageable);
}

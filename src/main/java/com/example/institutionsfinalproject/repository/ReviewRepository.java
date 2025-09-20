package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.ReviewEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
}

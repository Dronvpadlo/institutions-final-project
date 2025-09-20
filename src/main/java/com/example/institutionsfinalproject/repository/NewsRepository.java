package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.NewsEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsRepository extends MongoRepository<NewsEntity, String> {
}

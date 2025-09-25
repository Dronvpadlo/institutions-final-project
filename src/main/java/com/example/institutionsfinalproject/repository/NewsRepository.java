package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.NewsEntity;
import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NewsRepository extends MongoRepository<NewsEntity, String> {
    @NonNull
    Page<NewsEntity> findAll(@NonNull Pageable pageable);
}

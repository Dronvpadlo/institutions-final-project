package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.ReviewEntity;
import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends MongoRepository<ReviewEntity, String> {
    @NonNull
    Page<ReviewEntity> findAll(@NonNull Pageable pageable);

    Optional<List<ReviewEntity>> findByInstitutionId(String institutionId);
}

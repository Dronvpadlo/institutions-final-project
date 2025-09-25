package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstitutionRepository extends MongoRepository<InstitutionEntity, String> {
    @NonNull
    Page<InstitutionEntity> findAll(@NonNull Pageable pageable);
}

package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.ModerationStatus;
import com.mongodb.lang.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstitutionRepository extends MongoRepository<InstitutionEntity, String> {
    @NonNull
    Page<InstitutionEntity> findAllByModerationStatus(ModerationStatus status, @NonNull Pageable pageable);

    Page<InstitutionEntity> findByNameContainingIgnoreCase(String name, @NonNull Pageable pageable);
}

package com.example.institutionsfinalproject.repository;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InstitutionRepository extends MongoRepository<InstitutionEntity, String> {
}

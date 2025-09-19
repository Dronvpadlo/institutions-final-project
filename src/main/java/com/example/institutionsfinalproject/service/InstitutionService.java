package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.mapper.InstitutionMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstitutionService {

    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;

    public InstitutionService(InstitutionRepository institutionRepository, InstitutionMapper institutionMapper){
        this.institutionRepository = institutionRepository;
        this.institutionMapper = institutionMapper;
    }

    public InstitutionDTO createInstitution(InstitutionDTO institutionDTO) {
        InstitutionEntity institutionEntity = institutionMapper.toEntity(institutionDTO);
        InstitutionEntity savedInstitution = institutionRepository.save(institutionEntity);
        return institutionMapper.toDto(savedInstitution);
    }


    public List<InstitutionDTO> getAllInstitution(){
        List<InstitutionEntity> institutions = institutionRepository.findAll();
        return institutions.stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());
    }
}

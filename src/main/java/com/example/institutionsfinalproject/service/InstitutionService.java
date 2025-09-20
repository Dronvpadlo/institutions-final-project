package com.example.institutionsfinalproject.service;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import com.example.institutionsfinalproject.mapper.InstitutionMapper;
import com.example.institutionsfinalproject.repository.InstitutionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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

    public Optional<InstitutionDTO> getInstitutionById(String id){
        return institutionRepository.findById(id)
                .map(institutionMapper::toDto);
    }


    public List<InstitutionDTO> getAllInstitutions(){
        List<InstitutionEntity> institutions = institutionRepository.findAll();
        return institutions.stream()
                .map(institutionMapper::toDto)
                .collect(Collectors.toList());
    }

    public void  deleteInstitutionById(String id){
        institutionRepository.deleteById(id);
    }

    public Optional<InstitutionDTO> updateInstitution(String id, InstitutionDTO institutionDTO){
        return institutionRepository.findById(id)
                .map(existingInstitution -> {
                    InstitutionEntity updatedEntity = institutionMapper.toEntity(institutionDTO);
                    updatedEntity.setId(existingInstitution.getId());
                    institutionRepository.save(updatedEntity);
                    return institutionMapper.toDto(updatedEntity);
                });
    }

    public Optional<InstitutionDTO> patchInstitution(String id, Map<String, Object> updates){
        return institutionRepository.findById(id)
                .map(institution -> {
                    updates.forEach((key, value) ->{
                        switch (key){
                            case "name": institution.setName((String) value); break;
                            case "location": institution.setLocation((String) value); break;
                            case "contacts": institution.setContacts((String) value); break;
                            case "averageCheck": institution.setAverageCheck((double) value); break;
                            case "rating": institution.setRating((double) value); break;
                            case "openAt": institution.setOpenAt(LocalTime.parse((String) value));break;
                            case "closeAt": institution.setCloseAt(LocalTime.parse((String) value));break;
                        }
                    });
                    InstitutionEntity updatedInstitution = institutionRepository.save(institution);
                    return institutionMapper.toDto(updatedInstitution);
                });
    }
}

package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    @Mapping(source = "openAt", target = "openAt")
    @Mapping(source = "closeAt", target = "closeAt")
    InstitutionDTO toDto (InstitutionEntity institutionEntity);

    @Mapping(source = "openAt", target = "openAt")
    @Mapping(source = "closeAt", target = "closeAt")
    InstitutionEntity toEntity (InstitutionDTO institutionDTO);
}

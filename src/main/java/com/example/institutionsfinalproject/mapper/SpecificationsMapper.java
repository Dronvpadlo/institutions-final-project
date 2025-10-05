package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.Specifications;
import com.example.institutionsfinalproject.entity.dto.SpecificationsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecificationsMapper {

    SpecificationsDTO toDTO(Specifications specifications);
    Specifications toEntity(SpecificationsDTO specificationsDTO);
}

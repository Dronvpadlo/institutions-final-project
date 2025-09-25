package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.InstitutionEntity;
import com.example.institutionsfinalproject.entity.dto.InstitutionDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface InstitutionMapper {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    @Mapping(source = "openAt", target = "openAt")
    @Mapping(source = "closeAt", target = "closeAt")
    @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "formatDate")
    @Mapping(source = "moderationStatus", target = "moderationStatus")
    InstitutionDTO toDto (InstitutionEntity institutionEntity);

    @Named("formatDate")
    default String formatDate(Instant instant) {
        if (instant == null) {
            return null;
        }
        return formatter.format(instant);
    }

    @Mapping(source = "openAt", target = "openAt")
    @Mapping(source = "closeAt", target = "closeAt")
    @Mapping(source = "moderationStatus", target = "moderationStatus")
    InstitutionEntity toEntity (InstitutionDTO institutionDTO);
}

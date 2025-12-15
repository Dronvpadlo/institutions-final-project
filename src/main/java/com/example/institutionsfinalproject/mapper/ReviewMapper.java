package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.ReviewEntity;
import com.example.institutionsfinalproject.entity.dto.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    @Mapping(source = "rating", target = "rating")
    ReviewDTO toDto (ReviewEntity reviewEntity);
    ReviewEntity toEntity (ReviewDTO reviewDTO);
}

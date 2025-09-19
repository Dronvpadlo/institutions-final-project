package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.ReviewEntity;
import com.example.institutionsfinalproject.entity.dto.ReviewDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewDTO toDto (ReviewEntity reviewEntity);
    ReviewEntity toEntity (ReviewDTO reviewDTO);
}

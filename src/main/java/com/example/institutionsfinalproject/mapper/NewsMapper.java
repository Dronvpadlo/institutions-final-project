package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.NewsEntity;
import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    NewsDTO toDto (NewsEntity newsEntity);
    NewsEntity toEntity (NewsDTO newsDTO);
}

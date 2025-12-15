package com.example.institutionsfinalproject.mapper;

import com.example.institutionsfinalproject.entity.NewsEntity;
import com.example.institutionsfinalproject.entity.dto.NewsDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NewsMapper {
    @Mapping(source = "date", target = "date")
    NewsDTO toDto (NewsEntity newsEntity);
    NewsEntity toEntity (NewsDTO newsDTO);
}

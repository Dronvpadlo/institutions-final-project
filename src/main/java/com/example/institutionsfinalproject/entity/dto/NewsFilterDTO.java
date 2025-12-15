package com.example.institutionsfinalproject.entity.dto;

import com.example.institutionsfinalproject.entity.NewsType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NewsFilterDTO {

    private NewsType type;

    private String sortBy = "date";

    private String sortDirection = "desc";
}

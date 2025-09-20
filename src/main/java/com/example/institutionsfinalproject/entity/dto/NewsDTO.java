package com.example.institutionsfinalproject.entity.dto;

import com.example.institutionsfinalproject.entity.NewsType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {
    private String id;
    private String title;
    private String description;
    private String date;
    private String institutionId;
    private NewsType type;
}

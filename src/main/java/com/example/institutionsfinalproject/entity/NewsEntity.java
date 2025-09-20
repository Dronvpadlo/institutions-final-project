package com.example.institutionsfinalproject.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "news")
public class NewsEntity {
    private String title;
    private String description;
    private String date;
    private String institutionId;
    private NewsType type;
}

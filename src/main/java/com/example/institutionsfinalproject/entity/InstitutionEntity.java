package com.example.institutionsfinalproject.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Document(collection = "institutions")
public class InstitutionEntity {

    @Id
    private String id;
    private String name;
    private String location;
    private String workingHour;
    private String contacts;
    private double averageCheck;
    private double rating;
    private List<String> photoUrls;
    private List<String> tags;
    private boolean isModerated;
    private Map<String, Long> statistics;
    private List<String> newsIds;
    private List<String> reviewsIds;

}

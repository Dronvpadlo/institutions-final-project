package com.example.institutionsfinalproject.entity.dto;

import com.example.institutionsfinalproject.entity.ModerationStatus;
import com.example.institutionsfinalproject.entity.Specifications;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {
    private String id;
    private String name;
    private String location;
    private String openAt;
    private String closeAt;
    private String contacts;
    private String createdAt;
    private double averageCheck;
    private double rating;
    private ModerationStatus moderationStatus;
    private List<String> photoUrls;
    private List<String> tags;
    private Map<String, Long> statistics;
    private List<String> newsIds;
    private List<String> reviewsIds;
    private Specifications specifications;
}

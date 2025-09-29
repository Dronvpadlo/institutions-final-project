package com.example.institutionsfinalproject.entity.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class InstitutionFilterDTO {

    private Double minRating;
    private String type;
    private Double minAverageCheck;
    private Double maxAverageCheck;
    private List<String> tags;
    private Boolean hasWifi;
    private Boolean hasParking;
    private Boolean hasLiveMusic;

    private List<String> sortBy;
    private List<String> sortDirection;
}

package com.example.institutionsfinalproject.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private String id;
    private String institutionId;
    private String customerId;
    private int rating;
    private String description;
    private double checkAmount;
}

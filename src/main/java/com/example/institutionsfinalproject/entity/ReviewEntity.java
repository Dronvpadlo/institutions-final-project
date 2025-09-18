package com.example.institutionsfinalproject.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
public class ReviewEntity {
    @Id
    private String id;
    private String institutionsId;
    private String customerId;
    private int rating;
    private String description;
    private double checkAmount;
}

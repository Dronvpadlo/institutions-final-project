package com.example.institutionsfinalproject.entity.dto;

import com.example.institutionsfinalproject.entity.ModerationStatus;
import com.example.institutionsfinalproject.entity.Statistics;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstitutionDTO {
    private String id;

    @NotBlank(message = "Institution`s name cannot be empty")
    @Size(min = 2, max = 70, message = "Institution`s name must contained at least 2 and max 70 symbols")
    private String name;

    @NotBlank(message = "Location must be exist")
    private String location;

    @NotNull(message = "Open time must be exist")
    private LocalTime openAt;

    @NotNull(message = "Close time must be exist")
    private LocalTime closeAt;

    @NotBlank(message = "Contacts is required")
    private String contacts;

    private String createdAt;

    @DecimalMin(value = "0.2", message = "Average check must be greatest or equal than 0.2")
    @DecimalMax(value = "200000.00", message = "Average check must be less or equal than 200000.00")
    private double averageCheck;

    private double rating;

    private ModerationStatus moderationStatus;

    private List<String> photoUrls;

    private List<String> tags;

    private Statistics statistics;

    private List<String> newsIds;

    private List<String> reviewsIds;

    @Valid
    @NotNull(message = "Specifications must be exist")
    private SpecificationsDTO specifications;

    // todo will be getting id from token after security realized
    private String ownerId;
}

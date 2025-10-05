package com.example.institutionsfinalproject.entity.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    private String id;

    @NotNull(message = "Institution must be exist")
    private String institutionId;

    // todo will be id from token after realizing security
    private String customerId;

    @Min(value = 1, message = "rating must be greater or equal 1")
    @Max(value = 5, message = "rating must be less or equal 5")
    private Integer rating;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 3, max = 600, message = "Title must include more than 2 and less than 601 characters")
    private String description;

    @DecimalMin(value = "0.2", message = "Average check must be greatest or equal than 0.2")
    @DecimalMax(value = "200000.00", message = "Average check must be less or equal than 200000.00")
    private double checkAmount;
}

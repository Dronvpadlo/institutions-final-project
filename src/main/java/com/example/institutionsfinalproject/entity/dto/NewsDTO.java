package com.example.institutionsfinalproject.entity.dto;

import com.example.institutionsfinalproject.entity.NewsType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewsDTO {
    private String id;

    @NotBlank(message = "Title cannot be empty")
    @Size(min = 2, max = 30, message = "Title must include more or equal 2 and less or equal 30 characters")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    @Size(min = 3, max = 600, message = "Title must include more or equal 3 and less or equal 600 characters")
    private String description;

    @NotBlank(message = "Date cannot be empty")
    private LocalTime date;

    @NotNull(message = "Institution is required")
    private String institutionId;

    @NotNull(message = "Type is required")
    private NewsType type;
}
